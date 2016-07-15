/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.legacy

import net.bdew.lib.capabilities.Capabilities
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.{FluidStack, IFluidContainerItem}

trait OldFluidContainerItemEmulator extends Item with IFluidContainerItem {
  private def getCap(stack: ItemStack): Option[IFluidHandler] =
    if (stack.hasCapability(Capabilities.CAP_FLUID_HANDLER, null))
      Option(stack.getCapability(Capabilities.CAP_FLUID_HANDLER, null))
    else None

  override def getCapacity(container: ItemStack): Int =
    getCap(container).flatMap(_.getTankProperties.headOption).map(_.getCapacity).getOrElse(0)
  override def getFluid(container: ItemStack): FluidStack =
    getCap(container).map(_.drain(Int.MaxValue, false)).orNull
  override def fill(container: ItemStack, resource: FluidStack, doFill: Boolean): Int =
    getCap(container).map(_.fill(resource, doFill)).getOrElse(0)
  override def drain(container: ItemStack, maxDrain: Int, doDrain: Boolean): FluidStack =
    getCap(container).map(_.drain(maxDrain, doDrain)).orNull
}
