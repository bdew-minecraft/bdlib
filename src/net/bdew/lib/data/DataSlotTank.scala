/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids._

abstract class DataSlotTankBase(sz: Int) extends FluidTank(sz) with IFluidTank with DataSlot {
  var oldStack: FluidStack = null

  parent.onServerTick(checkUpdate)

  updateKind = Set(UpdateKind.GUI, UpdateKind.SAVE)

  val sendCapacityOnUpdateKind = Set(UpdateKind.GUI)

  def checkUpdate() {
    if (!isSame(oldStack, fluid)) {
      oldStack = if (fluid == null) null else fluid.copy()
      parent.dataSlotChanged(this)
    }
  }

  def isSame(v1: FluidStack, v2: FluidStack): Boolean = {
    if (v1 == null && v2 == null) return true
    if (v1 == null || v2 == null) return false
    return v1.isFluidStackIdentical(v2)
  }

  def save(t: NBTTagCompound, k: UpdateKind.Value) {
    val tag = new NBTTagCompound()
    writeToNBT(tag)
    if (sendCapacityOnUpdateKind.contains(k))
      tag.setInteger("size", getCapacity)
    t.setTag(name, tag)
  }

  def load(t: NBTTagCompound, k: UpdateKind.Value) {
    setFluid(null)
    val tag = t.getCompoundTag(name)
    readFromNBT(tag)
    if (sendCapacityOnUpdateKind.contains(k))
      setCapacity(tag.getInteger("size"))
  }

  override def setFluid(fluid: FluidStack) = execWithChangeNotify(super.setFluid(fluid))
  override def setCapacity(capacity: Int) = execWithChangeNotify(super.setCapacity(capacity))

  override def drain(maxDrain: Int, doDrain: Boolean) =
    if (doDrain)
      execWithChangeNotifyConditional[FluidStack](super.drain(maxDrain, true), _ != null)
    else
      super.drain(maxDrain, false)

  override def fill(resource: FluidStack, doFill: Boolean) =
    if (doFill)
      execWithChangeNotifyConditional[Int](super.fill(resource, true), _ > 0)
    else
      super.fill(resource, false)
}

case class DataSlotTank(name: String, parent: DataSlotContainer, size: Int) extends DataSlotTankBase(size)

case class DataSlotTankRestricted(name: String, parent: DataSlotContainer, var size: Int, filterFluid: Fluid) extends DataSlotTankBase(size) {
  def fill(amount: Int, doFill: Boolean) = super.fill(new FluidStack(filterFluid, amount), doFill)

  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    if ((resource != null) && (resource.getFluid != filterFluid)) return 0
    return super.fill(resource, doFill)
  }
}
