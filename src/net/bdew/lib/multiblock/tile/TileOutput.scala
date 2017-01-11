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
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.interact.{CIOutputFaces, MIOutput}
import net.bdew.lib.tile.TileTicking
import net.minecraft.util.EnumFacing

abstract class TileOutput[T <: OutputConfig] extends TileModule with MIOutput[T] with TileTicking {
  override def getCore = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: EnumFacing): T

  var rescanFaces = false

  def getCfg(dir: EnumFacing): Option[T] =
    for {
      core <- getCore
      oNum <- core.outputFaces.get(BlockFace(pos, dir))
      cfgGen <- core.outputConfig.get(oNum)
      cfg <- Misc.asInstanceOpt(cfgGen, outputConfigType)
    } yield cfg

  serverTick.listen(() => {
    if (rescanFaces) {
      rescanFaces = false
      doRescanFaces()
    }
  })

  override def tryConnect() {
    super.tryConnect()
    if (connected.isDefined) rescanFaces = true
  }

  def canConnectToFace(d: EnumFacing): Boolean

  def onConnectionsChanged(added: Set[EnumFacing], removed: Set[EnumFacing]) {}

  def doRescanFaces() {
    getCore foreach { core =>
      val connections = (
        EnumFacing.values()
          filterNot { dir => core.modules.contains(pos.offset(dir)) }
          filter canConnectToFace
        ).toSet
      val known = core.outputFaces.filter(_._1.pos == pos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(pos, x))
      toAdd.foreach(x => core.newOutput(pos, x, makeCfgObject(x)))
      if (toAdd.nonEmpty || toRemove.nonEmpty) {
        onConnectionsChanged(toAdd, toRemove)
        sendUpdateToClients()
      }
    }
  }
}
