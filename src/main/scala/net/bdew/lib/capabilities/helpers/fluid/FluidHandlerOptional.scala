package net.bdew.lib.capabilities.helpers.fluid

import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.IFluidHandler

class FluidHandlerOptional(handler: () => Option[IFluidHandler]) extends FluidHandlerProxy {
  override def base: IFluidHandler = handler().getOrElse(FluidHandlerNull)
}

object FluidHandlerOptional {
  def create(base: () => Option[IFluidHandler]): LazyOptional[IFluidHandler] = {
    val handler = new FluidHandlerOptional(base)
    LazyOptional.of(() => handler)
  }
}
