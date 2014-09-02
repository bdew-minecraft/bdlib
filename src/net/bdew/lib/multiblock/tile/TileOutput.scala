/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.interact.{CIOutputFaces, MIOutput}
import net.minecraftforge.common.util.ForgeDirection

abstract class TileOutput extends TileModule with MIOutput {
  override def getCore = getCoreAs[CIOutputFaces]

  def makeCfgObject(face: ForgeDirection): OutputConfig

  var rescanFaces = false

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

  def canConnectoToFace(d: ForgeDirection): Boolean

  def onConnectionsChanged(added: Set[ForgeDirection], removed: Set[ForgeDirection]) {}

  def doRescanFaces() {
    getCore map { core =>
      val connections = ForgeDirection.VALID_DIRECTIONS.filter(canConnectoToFace).toSet
      val known = core.outputFaces.filter(_._1.origin == mypos).map(_._1.face).toSet
      val toAdd = connections -- known
      val toRemove = known -- connections
      toRemove.foreach(x => core.removeOutput(mypos, x))
      toAdd.foreach(x => core.newOutput(mypos, x, makeCfgObject(x)))
      if (toAdd.size > 0 || toRemove.size > 0) {
        onConnectionsChanged(toAdd, toRemove)
        getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      }
    }
  }
}
