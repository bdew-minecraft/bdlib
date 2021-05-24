package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

/**
 * Empty FluidHandler that does nothing
 */
object FluidHandlerNull extends IFluidHandler {
  override def getTanks: Int = 0
  override def getFluidInTank(tank: Int): FluidStack = FluidStack.EMPTY
  override def getTankCapacity(tank: Int): Int = 0
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = false
  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = 0
  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
}
