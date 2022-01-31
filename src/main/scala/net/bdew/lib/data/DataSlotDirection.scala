package net.bdew.lib.data

import net.bdew.lib.data.base.DataSlotContainer
import net.bdew.lib.data.mixins.DataSlotNBT
import net.bdew.lib.nbt.Type
import net.bdew.lib.nbt.converters.TDirection
import net.minecraft.core.Direction

case class DataSlotDirection(name: String, parent: DataSlotContainer, default: Direction = Direction.UP) extends DataSlotNBT[Direction] {
  override def nbtType: Type[Direction] = TDirection
}
