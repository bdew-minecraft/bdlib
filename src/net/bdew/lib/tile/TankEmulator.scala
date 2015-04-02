/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.bdew.lib.Misc
import net.bdew.lib.data.base.DataSlotNumeric
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo, IFluidTank}

case class TankEmulator[T: Numeric](fluid: Fluid, ds: DataSlotNumeric[T], capacity: Int) extends IFluidTank {
  val num = implicitly[Numeric[T]]

  override def getFluidAmount: Int = num.toDouble(ds).floor.toInt
  override def getCapacity: Int = capacity

  override def getFluid: FluidStack = if (getFluidAmount > 0) new FluidStack(fluid, getFluidAmount) else null

  override def getInfo: FluidTankInfo = new FluidTankInfo(this)

  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = {
    val canDrain = Misc.clamp(maxDrain, 0, getFluidAmount)
    if (canDrain > 0) {
      if (doDrain) ds -= canDrain
      new FluidStack(fluid, canDrain)
    } else null
  }

  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    if (resource != null && resource.getFluid == fluid && resource.amount > 0) {
      val canFill = Misc.min(resource.amount, getCapacity - getFluidAmount)
      if (doFill) ds += canFill
      canFill
    } else 0
  }
}
