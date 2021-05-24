package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

/**
 * Pass-through fluid handler, used as a base for others
 */
trait FluidHandlerProxy extends IFluidHandler {
  def base: IFluidHandler

  override def getTanks: Int = base.getTanks
  override def getFluidInTank(tank: Int): FluidStack = base.getFluidInTank(tank)
  override def getTankCapacity(tank: Int): Int = base.getTankCapacity(tank)
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = base.isFluidValid(tank, stack)
  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = base.fill(resource, action)
  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = base.drain(resource, action)
  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = base.drain(maxDrain, action)
}
