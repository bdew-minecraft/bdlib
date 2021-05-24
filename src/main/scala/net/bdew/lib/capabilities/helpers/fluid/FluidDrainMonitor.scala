package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

/**
 * Wraps another fluid handler and calls the passed onDrain function when fluid is drained
 */
class FluidDrainMonitor(val base: IFluidHandler, onDrain: FluidStack => Unit) extends FluidHandlerProxy {
  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = {
    val tmp = super.drain(resource, action)
    if (!tmp.isEmpty && tmp.getAmount > 0 && action.execute) onDrain(tmp)
    tmp
  }

  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = {
    val tmp = super.drain(maxDrain, action)
    if (!tmp.isEmpty && tmp.getAmount > 0 && action.execute) onDrain(tmp)
    tmp
  }
}
