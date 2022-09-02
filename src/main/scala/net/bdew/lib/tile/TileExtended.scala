package net.bdew.lib.tile

import net.bdew.lib.{Event, Event1}
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState

class TileExtended(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends BlockEntity(teType, pos, state) {
  val persistSave: Event1[CompoundTag] = Event[CompoundTag]
  val persistLoad: Event1[CompoundTag] = Event[CompoundTag]
  val sendClientUpdate: Event1[CompoundTag] = Event[CompoundTag]
  val handleClientUpdate: Event1[CompoundTag] = Event[CompoundTag]

  override final def saveAdditional(tag: CompoundTag): Unit = {
    super.saveAdditional(tag)
    persistSave.trigger(tag)
  }

  override final def load(tag: CompoundTag): Unit = {
    super.load(tag)
    if (this.getLevel == null || !this.getLevel.isClientSide)
      persistLoad.trigger(tag)
  }

  override def handleUpdateTag(tag: CompoundTag): Unit = {
    handleClientUpdate.trigger(tag)
    super.handleUpdateTag(tag)
  }

  def sendUpdateToClients(): Unit = {
    val state = level.getBlockState(worldPosition)
    level.sendBlockUpdated(worldPosition, state, state, 3)
  }

  override final def getUpdatePacket: ClientboundBlockEntityDataPacket = {
    if (sendClientUpdate.hasListeners) {
      ClientboundBlockEntityDataPacket.create(this)
    } else null
  }

  override def getUpdateTag: CompoundTag = {
    val tag = super.getUpdateTag
    sendClientUpdate.trigger(tag)
    tag
  }

  override def onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket): Unit = {
    if (pkt.getTag != null)
      handleClientUpdate.trigger(pkt.getTag)
  }
}

