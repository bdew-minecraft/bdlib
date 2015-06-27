/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.bdew.lib.tile.TileExtended
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity

trait TileDataSlots extends TileExtended with DataSlotContainer {
  persistSave.listen(doSave(UpdateKind.SAVE, _))
  persistLoad.listen(doLoad(UpdateKind.SAVE, _))
  sendClientUpdate.listen(doSave(UpdateKind.WORLD, _))
  handleClientUpdate.listen { tag =>
    doLoad(UpdateKind.WORLD, tag)
    if (dataSlots.values.exists(_.updateKind.contains(UpdateKind.RENDER)))
      getWorldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord)
  }

  override def getWorldObject = getWorldObj

  override def onServerTick(f: () => Unit) = serverTick.listen(f)

  override def dataSlotChanged(slot: DataSlot) = {
    if (getWorldObj != null) {
      if (slot.updateKind.contains(UpdateKind.GUI))
        lastChange = getWorldObj.getTotalWorldTime
      if (!getWorldObj.isRemote && slot.updateKind.contains(UpdateKind.WORLD))
        getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      if (slot.updateKind.contains(UpdateKind.SAVE))
        getWorldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this)
    }
  }

  def getDataSlotPacket: Packet = {
    val tag = new NBTTagCompound()
    doSave(UpdateKind.GUI, tag)
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, ACT_GUI, tag)
  }

  override protected def extDataPacket(id: Int, data: NBTTagCompound) {
    if (id == ACT_GUI)
      doLoad(UpdateKind.GUI, data)
    super.extDataPacket(id, data)
  }
}
