package net.bdew.lib.data.base

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

import scala.collection.mutable

/**
 * Base Trait for objects that can contain Data Slots, this allows implementing them on things that aren't TEs
 */
trait DataSlotContainer {

  // Can't use getWorld - it fails to get remapped and stuff breaks in obfuscated environment
  def getWorldObject: Level

  /**
   * Called when a dataslot value changes
   */
  def dataSlotChanged(slot: DataSlot): Unit

  /**
   * List of dataslots in this container
   */
  val dataSlots = mutable.HashMap.empty[String, DataSlot]

  /**
   * Game time when the last change happened
   */
  var lastChange = 0L

  /**
   * Game time when the last GUI packet was sent
   */
  var lastGuiPacket = 0L

  /**
   * Check if an entity is withing range of a container
   *
   * @return true if entity is closer than range meters
   */
  def isEntityInRange(entity: Entity, range: Double): Boolean

  final val TRACE = false

  def doSave(kind: UpdateKind.Value, t: CompoundTag): Unit = {
    if (kind == UpdateKind.GUI)
      t.putLong("BDLib_TS", getWorldObject.getGameTime)
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      s.save(t, kind)
      if (TRACE) printf("%s (%s): %s S=> %s\n".format(kind, Thread.currentThread().getName, n, t.get(n)))
    }
  }

  def doLoad(kind: UpdateKind.Value, t: CompoundTag): Unit = {
    if (kind == UpdateKind.GUI) {
      val ts = t.getLong("BDLib_TS")
      if (ts > lastGuiPacket) {
        lastGuiPacket = ts
      } else {
        return
      }
    }
    for ((n, s) <- dataSlots if s.updateKind.contains(kind)) {
      if (TRACE) printf("%s (%s): %s L<= %s\n".format(kind, Thread.currentThread().getName, n, t.get(n)))
      s.load(t, kind)
    }
  }
}






