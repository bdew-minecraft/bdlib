/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.bdew.lib.Event
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

class TileExtended extends TileEntity {
  final val ACT_CLIENT = 1
  final val ACT_GUI = 2

  val persistSave = Event[NBTTagCompound]
  val persistLoad = Event[NBTTagCompound]
  val sendClientUpdate = Event[NBTTagCompound]
  val handleClientUpdate = Event[NBTTagCompound]

  override final def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    persistSave.trigger(tag)
  }

  override final def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    persistLoad.trigger(tag)
  }

  override final def getDescriptionPacket = {
    if (sendClientUpdate.hasListeners) {
      val tag = new NBTTagCompound
      sendClientUpdate.trigger(tag)
      new S35PacketUpdateTileEntity(pos, ACT_CLIENT, tag)
    } else null
  }

  override final def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
    if (pkt.getTileEntityType == ACT_CLIENT)
      handleClientUpdate.trigger(pkt.getNbtCompound)
    else
      extDataPacket(pkt.getTileEntityType, pkt.getNbtCompound)
  }

  protected def extDataPacket(id: Int, data: NBTTagCompound) {}
}

