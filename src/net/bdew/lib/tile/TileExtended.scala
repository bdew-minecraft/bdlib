/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.bdew.lib.Event
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity

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

  override final def getDescriptionPacket: Packet = {
    if (!sendClientUpdate.hasListeners) return null
    val tag = new NBTTagCompound
    sendClientUpdate.trigger(tag)
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, ACT_CLIENT, tag)
  }

  override final def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
    if (pkt.func_148853_f == ACT_CLIENT)
      handleClientUpdate.trigger(pkt.func_148857_g)
    else
      extDataPacket(pkt.func_148853_f, pkt.func_148857_g)
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
