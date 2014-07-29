/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlot, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{FluidStack, FluidTank, IFluidTank}

abstract class DataSlotTankBase(sz: Int) extends FluidTank(sz) with IFluidTank with DataSlot {
  var oldStack: FluidStack = null

  parent.serverTick.listen(checkUpdate)

  updateKind = Set(UpdateKind.GUI, UpdateKind.SAVE)

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
    if (k == UpdateKind.GUI)
      tag.setInteger("size", getCapacity)
    t.setTag(name, tag)
  }

  def load(t: NBTTagCompound, k: UpdateKind.Value) {
    setFluid(null)
    val tag = t.getCompoundTag(name)
    readFromNBT(tag)
    if (k == UpdateKind.GUI)
      setCapacity(tag.getInteger("size"))
  }
}

case class DataSlotTank(name: String, parent: TileDataSlots, size: Int) extends DataSlotTankBase(size)

case class DataSlotTankRestricted(name: String, parent: TileDataSlots, var size: Int, fluidID: Int) extends DataSlotTankBase(size) {
  def fill(amount: Int, doFill: Boolean) = super.fill(new FluidStack(fluidID, amount), doFill)

  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    if ((resource != null) && (resource.fluidID != fluidID)) return 0
    return super.fill(resource, doFill)
  }
}
