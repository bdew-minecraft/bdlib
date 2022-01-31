package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.CompoundTag

case class DataSlotEnum[T <: Enumeration](name: String, parent: DataSlotContainer, baseEnum: T) extends DataSlotVal[T#Value] {
  setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  override def default: T#Value = baseEnum(0)

  override def loadValue(t: CompoundTag, kind: UpdateKind.Value): T#Value =
    baseEnum(t.getByte(name))

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit =
    t.putByte(name, value.id.toByte)
}
