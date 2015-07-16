/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tooltip

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack, IFluidContainerItem}

trait FluidTooltipProvider extends TooltipProvider {
  def getFluid(is: ItemStack): Option[FluidStack] = {
    if (is.getItem.isInstanceOf[IFluidContainerItem]) {
      Option(is.getItem.asInstanceOf[IFluidContainerItem].getFluid(is))
    } else {
      Option(FluidContainerRegistry.getFluidForFilledItem(is))
    }
  }

  override def shouldHandleTooltip(stack: ItemStack): Boolean = getFluid(stack) exists shouldHandleTooltip
  override def handleTooltip(stack: ItemStack, advanced: Boolean, shift: Boolean): Iterable[String] =
    getFluid(stack) map (f => handleTooltip(f, advanced, shift)) getOrElse Iterable.empty

  def shouldHandleTooltip(fluid: FluidStack): Boolean
  def handleTooltip(fluid: FluidStack, advanced: Boolean, shift: Boolean): Iterable[String]
}
