package net.bdew.lib.tile

import net.bdew.lib.Misc
import net.bdew.lib.data.mixins.DataSlotNumeric
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.fluids.{FluidStack, IFluidTank}

case class TankEmulator[T: Numeric](fluid: Fluid, ds: DataSlotNumeric[T], capacity: Int) extends IFluidTank with IFluidHandler {
  val num: Numeric[T] = implicitly[Numeric[T]]

  override def getFluidAmount: Int = num.toDouble(ds).floor.toInt
  override def getCapacity: Int = capacity
  override def getFluid: FluidStack = if (getFluidAmount > 0) new FluidStack(fluid, getFluidAmount) else null

  override def drain(maxDrain: Int, action: FluidAction): FluidStack = {
    val canDrain = Misc.clamp(maxDrain, 0, getFluidAmount)
    if (canDrain > 0) {
      if (action.execute) ds -= canDrain
      new FluidStack(fluid, canDrain)
    } else null
  }

  override def fill(resource: FluidStack, action: FluidAction): Int = {
    if (resource != null && resource.getFluid == fluid && resource.getAmount > 0) {
      val canFill = Misc.min(resource.getAmount, getCapacity - getFluidAmount)
      if (action.execute) ds += canFill
      canFill
    } else 0
  }

  override def drain(resource: FluidStack, action: FluidAction): FluidStack = {
    if (resource != null && resource.getFluid == fluid) {
      drain(resource.getAmount, action)
    } else null
  }

  override def getTanks: Int = 1
  override def getFluidInTank(tank: Int): FluidStack = drain(Int.MaxValue, FluidAction.SIMULATE)
  override def getTankCapacity(tank: Int): Int = capacity
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = stack.getFluid == fluid
  override def isFluidValid(stack: FluidStack): Boolean = stack.getFluid == fluid
}

