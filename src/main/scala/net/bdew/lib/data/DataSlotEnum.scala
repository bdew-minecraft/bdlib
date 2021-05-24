package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.CompoundNBT

case class DataSlotEnum[T <: Enumeration](name: String, parent: DataSlotContainer, enum: T) extends DataSlotVal[T#Value] {
  setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  override def default: T#Value = enum(0)

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): T#Value =
    enum(t.getByte(name))

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit =
    t.putByte(name, value.id.toByte)
}
