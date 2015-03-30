/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{FluidStack, FluidTankInfo}

class DataSlotResource(val name: String, val parent: DataSlotContainer, initCapacity: Int) extends DataSlot {
  var resource: Option[Resource] = None
  var capacity = initCapacity
  val sendCapacityOnUpdateKind = Set(UpdateKind.GUI)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def getEffectiveCapacity = resource.map(_.kind.capacityMultiplier).getOrElse(0D) * capacity

  /**
   * Low level fill method that operates on Resource's
   * @param res Resource to fill
   * @param onlyRound Only fill integer amounts
   * @param doFill If false, fill will only be simulated
   * @return how much was or would be filled
   */
  def rawFill(res: Resource, onlyRound: Boolean, doFill: Boolean) = {
    if (resource.isEmpty || resource.get.kind == res.kind) {
      val current = if (resource.isEmpty) 0D else resource.get.amount
      var canFill = Misc.clamp(res.amount, 0D, capacity * res.kind.capacityMultiplier - current)
      if (onlyRound) canFill = canFill.floor
      if (doFill && canFill > 0) execWithChangeNotify {
        resource = Some(Resource(res.kind, current + canFill))
      }
      canFill
    } else 0D
  }

  /**
   * Low level drain method that operates on Resource's
   * @param maxDrain maximum amount to drain
   * @param onlyRound Only fill integer amounts
   * @param doDrain If false, drain will only be simulated
   * @return how much was or would be drained
   */
  def rawDrain(maxDrain: Double, onlyRound: Boolean, doDrain: Boolean): Option[Resource] = {
    resource map { res =>
      var canDrain = Misc.clamp(res.amount, 0D, maxDrain)
      if (onlyRound) canDrain = canDrain.floor
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

  def fillFluid(resource: FluidStack, doFill: Boolean): Int = rawFill(Resource.from(resource), onlyRound = true, doFill).toInt
  def drainFluid(maxDrain: Int, doDrain: Boolean): FluidStack = {
    (for {
      res <- rawDrain(maxDrain, true, false)
      rKind <- Misc.asInstanceOpt(res.kind, classOf[FluidResource])
    } yield {
      if (doDrain)
        rawDrain(res.amount, false, true)
      new FluidStack(rKind.fluid, res.amount.toInt)
    }).orNull
  }
  def getTankInfo = {
    (for {
      res <- resource
      rKind <- Misc.asInstanceOpt(res.kind, classOf[FluidResource])
    } yield {
      new FluidTankInfo(new FluidStack(rKind.fluid, res.amount.toInt), getEffectiveCapacity.toInt)
    }).getOrElse(new FluidTankInfo(null, capacity))
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    val tag = t.getCompoundTag(name)

    resource = if (tag.hasKey("resource"))
      ResourceManager.loadFromNBT(tag.getCompoundTag("resource"))
    else
      None


    if (sendCapacityOnUpdateKind.contains(kind))
      tag.getInteger("capacity")
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val tag = new NBTTagCompound()

    resource foreach { res => tag.setTag("resource", ResourceManager.saveToNBT(res)) }

    if (sendCapacityOnUpdateKind.contains(kind))
      tag.setInteger("capacity", capacity)

    t.setTag(name, tag)
  }
}

