/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla.pimpModelData
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.interact.{CIOutputFaces, MIOutput}
import net.bdew.lib.multiblock.render.OutputFaceProperty
import net.bdew.lib.tile.{TileExtended, TileTicking}
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.client.model.data.IModelData

abstract class TileOutput[T <: OutputConfig](teType: TileEntityType[_]) extends TileExtended(teType)
  with TileModule with MIOutput[T] with TileTicking {

  override def getCore: Option[CIOutputFaces] = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: Direction): T

  var rescanFaces = false

  def getCfg(dir: Direction): Option[T] =
    for {
      core <- getCore
      oNum <- core.outputFaces.get(BlockFace(getBlockPos, dir))
      cfgGen <- core.outputConfig.get(oNum)
      cfg <- Misc.asInstanceOpt(cfgGen, outputConfigType)
    } yield cfg

  serverTick.listen(() => {
    if (rescanFaces) {
      rescanFaces = false
      doRescanFaces()
    }
  })

  override def tryConnect(): Unit = {
    super.tryConnect()
    if (connected.isDefined) rescanFaces = true
  }

  def canConnectToFace(d: Direction): Boolean

  def onConnectionsChanged(added: Set[Direction], removed: Set[Direction]): Unit = {}

  def doRescanFaces(): Unit = {
    getCore foreach { core =>
      val connections = (
        Direction.values()
          filterNot { dir => core.modules.contains(getBlockPos.offset(dir.getNormal)) }
          filter canConnectToFace
        ).toSet
      val known = core.outputFaces.filter(_._1.pos == getBlockPos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(getBlockPos, x))
      toAdd.foreach(x => core.newOutput(getBlockPos, x, makeCfgObject(x)))
      if (toAdd.nonEmpty || toRemove.nonEmpty) {
        onConnectionsChanged(toAdd, toRemove)
        sendUpdateToClients()
      }
    }
  }

  override def getModelData: IModelData = {
    val faces = for {
      core <- getCore.toIterable
      face <- Direction.values()
      output <- core.outputFaces.get(BlockFace(getBlockPos, face))
    } yield face -> output
    super.getModelData.withData(OutputFaceProperty, faces.toMap)
  }
}
