/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.bdew.lib.Client
import net.bdew.lib.tile.TileExtended
import net.minecraft.entity.Entity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.world.World

trait TileDataSlots extends TileExtended with DataSlotContainer {
  persistSave.listen(doSave(UpdateKind.SAVE, _))
  persistLoad.listen(doLoad(UpdateKind.SAVE, _))
  sendClientUpdate.listen(doSave(UpdateKind.WORLD, _))
  handleClientUpdate.listen { tag =>
    doLoad(UpdateKind.WORLD, tag)
    if (dataSlots.values.exists(_.updateKind.contains(UpdateKind.MODEL_DATA)))
      requestModelDataUpdate()
    if (dataSlots.values.exists(_.updateKind.contains(UpdateKind.RENDER)))
      Client.doRenderUpdate(getBlockPos)
  }

  override def getWorldObject: World = getLevel

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (getWorldObject != null) {
      if (slot.updateKind.contains(UpdateKind.GUI))
        lastChange = getWorldObject.getGameTime

      if (!getWorldObject.isClientSide && slot.updateKind.contains(UpdateKind.WORLD))
        sendUpdateToClients()

      if (slot.updateKind.contains(UpdateKind.SAVE))
        getWorldObject.blockEntityChanged(getBlockPos, this)

      if (slot.updateKind.contains(UpdateKind.UPDATE)) {
        getLevel.updateNeighborsAt(getBlockPos, getBlockState.getBlock)
      }
    }
  }

  def getDataSlotPacket: SUpdateTileEntityPacket = {
    val tag = new CompoundNBT()
    doSave(UpdateKind.GUI, tag)
    new SUpdateTileEntityPacket(getBlockPos, ACT_GUI, tag)
  }

  override protected def extDataPacket(id: Int, data: CompoundNBT): Unit = {
    if (id == ACT_GUI)
      doLoad(UpdateKind.GUI, data)
    super.extDataPacket(id, data)
  }

  override def isEntityInRange(entity: Entity, range: Double): Boolean =
    getWorldObject.getBlockEntity(getBlockPos) == this &&
      entity.distanceToSqr(getBlockPos.getX + 0.5D, getBlockPos.getY + 0.5D, getBlockPos.getZ + 0.5D) <= 64.0D
}

