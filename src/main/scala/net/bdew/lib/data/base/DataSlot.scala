package net.bdew.lib.data.base

import net.minecraft.nbt.CompoundTag

/**
 * Base trait for all data slots
 */
trait DataSlot {
  /**
   * Tile Entity that owns this slot
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val parent: DataSlotContainer

  /**
   * Unique name
   * Accessed in constructor, so should be in parameters or a lazy val, otherwise will crash
   */
  val name: String

  parent.dataSlots += (name -> this)

  // Where should it sync
  var updateKind = Set(UpdateKind.GUI)

  def save(t: CompoundTag, kind: UpdateKind.Value): Unit
  def load(t: CompoundTag, kind: UpdateKind.Value): Unit

  def execWithChangeNotify[T](f: => T): T = {
    val v = f
    parent.dataSlotChanged(this)
    v
  }

  def execWithChangeNotifyConditional[T](f: => T, condition: T => Boolean): T = {
    val v = f
    if (condition(v))
      parent.dataSlotChanged(this)
    v
  }

  def setUpdate(vals: UpdateKind.Value*): this.type = {
    updateKind = vals.toSet
    this
  }
}


