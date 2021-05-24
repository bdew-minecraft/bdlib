package net.bdew.lib.data.mixins

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.bdew.lib.nbt.Type
import net.minecraft.nbt.CompoundNBT

trait DataSlotNBT[T] extends DataSlotVal[T] {
  implicit def nbtType: Type[T]
  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit =
    if (value != null)
      t.setVal(name, value)
  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): T =
    t.getVal[T](name).getOrElse(default)
}

trait DataSlotNBTOption[T] extends DataSlotOption[T] {
  implicit def nbtType: Type[T]
  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit =
    value.foreach(v => t.setVal(name, v))
  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): Option[T] =
    t.getVal[T](name)
}

