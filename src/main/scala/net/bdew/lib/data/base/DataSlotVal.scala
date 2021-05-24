package net.bdew.lib.data.base

import net.minecraft.nbt.CompoundNBT

import scala.language.implicitConversions

/**
 * Base trait for data slots that hold primitive values
 * Defines some convenience operators to access the value and an implicit conversion to the value
 */

trait DataSlotVal[T] extends DataSlot {
  def default: T

  private var realValue = default

  def value: T = realValue

  def isSame(v: T): Boolean = v == value

  def :=(that: T): Unit = update(that)

  def :!=(that: T): Boolean = value != that
  def :==(that: T): Boolean = value == that

  def loadValue(t: CompoundNBT, kind: UpdateKind.Value): T

  final override def load(t: CompoundNBT, kind: UpdateKind.Value): Unit = realValue = loadValue(t, kind)

  def update(v: T, notify: Boolean = true): Unit = {
    if (!isSame(v)) {
      realValue = v
      if (notify)
        changed()
    }
  }

  def changed(): Unit = {
    parent.dataSlotChanged(this)
  }
}

object DataSlotVal {
  implicit def slot2val[T](slot: DataSlotVal[T]): T = slot.value
}