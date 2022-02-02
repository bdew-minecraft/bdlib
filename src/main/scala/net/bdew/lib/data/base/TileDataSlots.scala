package net.bdew.lib.data.base

import net.bdew.lib.Client
import net.bdew.lib.nbt.NBT
import net.bdew.lib.tile.TileExtended
import net.minecraft.nbt.Tag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

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

  override def getWorldObject: Level = getLevel

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (getWorldObject != null) {
      if (slot.updateKind.contains(UpdateKind.GUI))
        lastChange = getWorldObject.getGameTime

      if (!getWorldObject.isClientSide && slot.updateKind.contains(UpdateKind.WORLD))
        sendUpdateToClients()

      if (slot.updateKind.contains(UpdateKind.SAVE))
        getWorldObject.blockEntityChanged(getBlockPos)

      if (slot.updateKind.contains(UpdateKind.UPDATE)) {
        getLevel.updateNeighborsAt(getBlockPos, getBlockState.getBlock)
      }
    }
  }

  def getDataSlotPacket: ClientboundBlockEntityDataPacket = {
    ClientboundBlockEntityDataPacket.create(this, (x: BlockEntity) => {
      NBT from { tag =>
        doSave(UpdateKind.GUI, tag)
        tag.putByte("_gui_", 1);
      }
    })
  }

  override def onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket): Unit = {
    if (pkt.getTag != null && pkt.getTag.contains("_gui_", Tag.TAG_BYTE) && pkt.getTag.getByte("_gui_") == 1) {
      doLoad(UpdateKind.GUI, pkt.getTag)
    } else
      super.onDataPacket(net, pkt)
  }

  override def isEntityInRange(entity: Entity, range: Double): Boolean =
    getWorldObject.getBlockEntity(getBlockPos) == this &&
      entity.distanceToSqr(getBlockPos.getX + 0.5D, getBlockPos.getY + 0.5D, getBlockPos.getZ + 0.5D) <= 64.0D
}

