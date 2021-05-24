package net.bdew.lib.multiblock.data

import net.bdew.lib.data.base.{DataSlotContainer, UpdateKind}
import net.bdew.lib.data.mixins.DataSlotNBTOption
import net.bdew.lib.nbt.Type
import net.bdew.lib.nbt.converters.TBlockPos
import net.minecraft.util.math.BlockPos

case class DataSlotPos(name: String, parent: DataSlotContainer) extends DataSlotNBTOption[BlockPos] {
  override def nbtType: Type[BlockPos] = TBlockPos
  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)
}
