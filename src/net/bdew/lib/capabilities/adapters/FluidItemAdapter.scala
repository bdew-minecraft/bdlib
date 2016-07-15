/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.adapters

import net.bdew.lib.capabilities.CapAdapter
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.IFluidContainerItem
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.wrappers.FluidContainerItemWrapper

object FluidItemAdapter extends CapAdapter[IFluidHandler] {
  override def canWrap(stack: ItemStack): Boolean =
    stack != null && stack.getItem.isInstanceOf[IFluidContainerItem]

  override def wrap(stack: ItemStack): Option[IFluidHandler] =
    Some(new FluidContainerItemWrapper(stack.getItem.asInstanceOf[IFluidContainerItem], stack))
}
