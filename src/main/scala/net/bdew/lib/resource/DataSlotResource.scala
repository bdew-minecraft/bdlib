package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.{CompoundTag, Tag}

class DataSlotResource(val name: String, val parent: DataSlotContainer, initCapacity: Int, val canAccept: Resource => Boolean = _ => true) extends DataSlot {
  var resource: Option[Resource] = None
  var capacity: Int = initCapacity
  val sendCapacityOnUpdateKind = Set(UpdateKind.GUI)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def getEffectiveCapacity: Double = resource.map(_.kind.capacityMultiplier).getOrElse(0D) * capacity

  /**
   * Low level fill method that operates on Resources
   *
   * @param res       Resource to fill
   * @param onlyRound Only fill integer amounts
   * @param doFill    If false, fill will only be simulated
   * @return how much was or would be filled
   */
  def fill(res: Resource, onlyRound: Boolean, doFill: Boolean): Double = {
    if (!canAccept(res)) return 0
    if (resource.isEmpty || resource.get.kind == res.kind) {
      val current = if (resource.isEmpty) 0D else resource.get.amount
      var canFill = Misc.clamp(res.amount, 0D, capacity * res.kind.capacityMultiplier - current)
      if (onlyRound) canFill = canFill.floor
      if (canFill <= 0) return 0
      if (doFill && canFill > 0) execWithChangeNotify {
        resource = Some(Resource(res.kind, current + canFill))
      }
      canFill
    } else 0D
  }

  /**
   * Low level drain method that operates on Resources
   *
   * @param maxDrain  maximum amount to drain
   * @param onlyRound Only fill integer amounts
   * @param doDrain   If false, drain will only be simulated
   * @return how much was or would be drained
   */
  def drain(maxDrain: Double, onlyRound: Boolean, doDrain: Boolean): Option[Resource] = {
    resource map { res =>
      var canDrain = Misc.clamp(res.amount, 0D, maxDrain)
      if (onlyRound) canDrain = canDrain.floor
      if (canDrain <= 0) return None
      if (doDrain && canDrain > 0) execWithChangeNotify {
        resource =
          if (res.amount - canDrain < 0.000001)
            None
          else
            Some(Resource(res.kind, res.amount - canDrain))
      }
      Resource(res.kind, canDrain)
    }
  }

  override def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    val tag = t.getCompound(name)

    resource = if (tag.contains("resource", Tag.TAG_COMPOUND))
      Resource.loadFromNBT(tag.getCompound("resource"))
    else
      None

    if (sendCapacityOnUpdateKind.contains(kind))
      tag.getInt("capacity")
  }

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    val tag = new CompoundTag()

    resource foreach { res => tag.put("resource", Resource.saveToNBT(res)) }

    if (sendCapacityOnUpdateKind.contains(kind))
      tag.putInt("capacity", capacity)

    t.put(name, tag)
  }
}

