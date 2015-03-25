/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.multiblock.Tools
import net.bdew.lib.multiblock.data.DataSlotPos

import scala.reflect.ClassTag

trait TileModule extends TileDataSlots {
  val kind: String
  val connected = new DataSlotPos("connected", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  def getCoreAs[T <: TileController : ClassTag] = connected flatMap (_.getTile[T](getWorldObj))

  def getCore = getCoreAs[TileController]

  lazy val myPos = BlockRef(xCoord, yCoord, zCoord)

  def connect(target: TileController) {
    if (target.moduleConnected(this)) {
      connected.set(target.myPos)
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      getWorldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType)
    }
  }

  def coreRemoved() {
    connected.unset()
    getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    getWorldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType)
  }

  def onBreak() {
    getCore foreach (_.moduleRemoved(this))
  }

  def canConnectToCore(br: BlockRef) = true

  def tryConnect() {
    if (getCore.isEmpty) {
      for {
        conn <- Tools.findConnections(getWorldObj, myPos, kind).headOption if canConnectToCore(conn)
        core <- conn.getTile[TileController](getWorldObj)
      } {
        connect(core)
      }
    }
  }
}
