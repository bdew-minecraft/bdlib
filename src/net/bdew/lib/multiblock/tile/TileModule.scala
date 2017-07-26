/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.multiblock.Tools
import net.bdew.lib.multiblock.data.DataSlotPos
import net.minecraft.util.math.BlockPos

import scala.reflect.ClassTag

trait TileModule extends TileDataSlots {
  val kind: String
  val connected = DataSlotPos("connected", this).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE, UpdateKind.RENDER)

  def getCoreAs[T <: TileController : ClassTag] = connected flatMap (p => getWorld.getTileSafe[T](p))

  def getCore = getCoreAs[TileController]

  def connect(target: TileController) {
    if (target.moduleConnected(this)) {
      connected.set(target.getPos)
      sendUpdateToClients()
      getWorld.notifyNeighborsOfStateChange(getPos, getBlockType, true)
    }
  }

  def coreRemoved() {
    connected.unset()
    sendUpdateToClients()
    getWorld.notifyNeighborsOfStateChange(getPos, getBlockType, true)
  }

  def onBreak() {
    getCore foreach (_.moduleRemoved(this))
  }

  def canConnectToCore(br: BlockPos) = true

  def tryConnect() {
    if (getCore.isEmpty) {
      for {
        conn <- Tools.findConnections(getWorld, getPos, kind).headOption if canConnectToCore(conn)
        core <- getWorld.getTileSafe[TileController](conn)
      } {
        connect(core)
      }
    }
  }
}
