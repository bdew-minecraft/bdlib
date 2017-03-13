/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.bdew.lib.tile.TileExtended
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.SPacketUpdateTileEntity

trait TileDataSlots extends TileExtended with DataSlotContainer {
  persistSave.listen(doSave(UpdateKind.SAVE, _))
  persistLoad.listen(doLoad(UpdateKind.SAVE, _))
  sendClientUpdate.listen(doSave(UpdateKind.WORLD, _))
  handleClientUpdate.listen { tag =>
    doLoad(UpdateKind.WORLD, tag)
    if (dataSlots.values.exists(_.updateKind.contains(UpdateKind.RENDER)))
      getWorld.markBlockRangeForRenderUpdate(getPos, getPos)
  }

  override def getWorldObject = getWorld

  override def dataSlotChanged(slot: DataSlot) = {
    if (getWorld != null) {
      if (slot.updateKind.contains(UpdateKind.GUI))
        lastChange = getWorld.getTotalWorldTime
      if (!getWorld.isRemote && slot.updateKind.contains(UpdateKind.WORLD))
        sendUpdateToClients()
      if (slot.updateKind.contains(UpdateKind.SAVE))
        getWorld.markChunkDirty(getPos, this)
    }
  }

  def getDataSlotPacket = {
    val tag = new NBTTagCompound()
    doSave(UpdateKind.GUI, tag)
    new SPacketUpdateTileEntity(getPos, ACT_GUI, tag)
  }

  override protected def extDataPacket(id: Int, data: NBTTagCompound) {
    if (id == ACT_GUI)
      doLoad(UpdateKind.GUI, data)
    super.extDataPacket(id, data)
  }

  override def isEntityInRange(entity: Entity, range: Double): Boolean =
    getWorld.getTileEntity(getPos) == this && entity.getDistanceSqToCenter(getPos) <= range
}

