/*
 * Copyright (c) bdew, 2013 - 2014
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
import net.minecraftforge.common.util.ForgeDirection

abstract class TileOutput[T <: OutputConfig] extends TileModule with MIOutput[T] {
  override def getCore = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: ForgeDirection): T

  var rescanFaces = false

  def getCfg(dir: ForgeDirection): Option[T] =
    for {
      core <- getCore
      oNum <- core.outputFaces.get(BlockFace(myPos, dir))
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

  def canConnectToFace(d: ForgeDirection): Boolean

  def onConnectionsChanged(added: Set[ForgeDirection], removed: Set[ForgeDirection]) {}

  def doRescanFaces() {
    getCore foreach { core =>
      val connections = (
        ForgeDirection.VALID_DIRECTIONS
          filterNot { dir => core.modules.contains(myPos.neighbour(dir)) }
          filter canConnectToFace
        ).toSet
      val known = core.outputFaces.filter(_._1.origin == myPos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(myPos, x))
      toAdd.foreach(x => core.newOutput(myPos, x, makeCfgObject(x)))
      if (toAdd.nonEmpty || toRemove.nonEmpty) {
        onConnectionsChanged(toAdd, toRemove)
        getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      }
    }
  }
}
