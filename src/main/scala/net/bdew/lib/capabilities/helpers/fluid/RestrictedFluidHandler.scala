package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class RestrictedFluidHandler(val base: IFluidHandler, canFill: FluidStack => Boolean, canDrain: Boolean) extends FluidHandlerProxy {
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = canFill(stack) && super.isFluidValid(tank, stack)

  override def fill(resource: FluidStack, action: FluidAction): Int =
    if (canFill(resource))
      super.fill(resource, action)
    else
      0

  override def drain(resource: FluidStack, action: FluidAction): FluidStack =
    if (canDrain)
      super.drain(resource, action)
    else
      FluidStack.EMPTY

  override def drain(maxDrain: Int, action: FluidAction): FluidStack =
    if (canDrain)
      super.drain(maxDrain, action)
    else
      FluidStack.EMPTY
}

object RestrictedFluidHandler {
  def fillOnly(base: IFluidHandler) = new RestrictedFluidHandler(base, canFill = _ => true, false)
  def drainOnly(base: IFluidHandler) = new RestrictedFluidHandler(base, canFill = _ => false, true)
  def closed(base: IFluidHandler) = new RestrictedFluidHandler(base, canFill = _ => false, false)
}