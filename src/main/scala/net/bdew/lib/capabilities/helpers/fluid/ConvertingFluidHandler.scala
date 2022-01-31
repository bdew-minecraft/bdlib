package net.bdew.lib.capabilities.helpers.fluid

import net.minecraft.tags.Tag
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

class ConvertingFluidHandler(val base: IFluidHandler, from: Tag[Fluid], to: Fluid) extends FluidHandlerProxy {
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean =
    super.isFluidValid(tank, stack) || (from.contains(stack.getFluid) && super.isFluidValid(tank, new FluidStack(to, stack.getAmount)))

  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
    if (resource.getFluid != to && from.contains(resource.getFluid))
      super.fill(new FluidStack(to, resource.getAmount), action)
    else
      super.fill(resource, action)
}
