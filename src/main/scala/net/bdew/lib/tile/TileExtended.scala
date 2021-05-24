/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.bdew.lib.{Event, Event1}
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.{TileEntity, TileEntityType}

class TileExtended(t: TileEntityType[_]) extends TileEntity(t) {
  final val ACT_CLIENT = 1
  final val ACT_GUI = 2

  val persistSave: Event1[CompoundNBT] = Event[CompoundNBT]
  val persistLoad: Event1[CompoundNBT] = Event[CompoundNBT]
  val sendClientUpdate: Event1[CompoundNBT] = Event[CompoundNBT]
  val handleClientUpdate: Event1[CompoundNBT] = Event[CompoundNBT]

  override final def save(tag: CompoundNBT): CompoundNBT = {
    super.save(tag)
    persistSave.trigger(tag)
    tag
  }

  override final def load(bs: BlockState, tag: CompoundNBT): Unit = {
    super.load(bs, tag)
    persistLoad.trigger(tag)
  }

  def sendUpdateToClients(): Unit = {
    val state = level.getBlockState(worldPosition)
    level.sendBlockUpdated(worldPosition, state, state, 3)
  }

  override final def getUpdatePacket: SUpdateTileEntityPacket = {
    if (sendClientUpdate.hasListeners) {
      new SUpdateTileEntityPacket(worldPosition, ACT_CLIENT, getUpdateTag)
    } else null
  }

  override def getUpdateTag: CompoundNBT = {
    val tag = super.getUpdateTag
    sendClientUpdate.trigger(tag)
    tag
  }

  override def onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket): Unit = {
    if (pkt.getType == ACT_CLIENT)
      handleClientUpdate.trigger(pkt.getTag)
    else
      extDataPacket(pkt.getType, pkt.getTag)
  }

  override def handleUpdateTag(state: BlockState, tag: CompoundNBT): Unit = {
    handleClientUpdate.trigger(tag)
  }

  protected def extDataPacket(id: Int, data: CompoundNBT): Unit = {}
}

