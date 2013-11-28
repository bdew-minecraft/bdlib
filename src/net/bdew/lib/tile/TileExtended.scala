/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.INetworkManager
import net.minecraft.network.packet.Packet132TileEntityData
import net.bdew.lib.Event

class TileExtended extends TileEntity {
  final val ACT_CLIENT = 1
  final val ACT_GUI = 2

  var clientTick = Event()
  var serverTick = Event()
  var persistSave = Event[NBTTagCompound]
  var persistLoad = Event[NBTTagCompound]
  var sendClientUpdate = Event[NBTTagCompound]
  var handleClientUpdate = Event[NBTTagCompound]

  def tickClient() {}
  def tickServer() {}

  override final def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    persistSave.trigger(tag)
  }

  override final def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    persistLoad.trigger(tag)
  }

  override final def getDescriptionPacket: Packet132TileEntityData = {
    if (!sendClientUpdate.hasListeners) return null
    val tag = new NBTTagCompound
    sendClientUpdate.trigger(tag)
    return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, ACT_CLIENT, tag)
  }

  override final def onDataPacket(net: INetworkManager, pkt: Packet132TileEntityData) {
    if (pkt.actionType == ACT_CLIENT)
      handleClientUpdate.trigger(pkt.data)
    else
      extDataPacket(pkt.actionType, pkt.data)
  }

  override final def updateEntity() {
    if (worldObj != null) {
      if (worldObj.isRemote) {
        tickClient()
        clientTick.trigger()
      } else {
        tickServer()
        serverTick.trigger()
      }
    }
  }

  protected def extDataPacket(id: Int, data: NBTTagCompound) {}
}
