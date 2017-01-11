/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.helpers

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidTankProperties}

/**
  * Pass-through fluid handler, used as a base for others
  */
trait FluidHandlerProxy extends IFluidHandler {
  def base: IFluidHandler
  override def getTankProperties: Array[IFluidTankProperties] = base.getTankProperties
  override def drain(resource: FluidStack, doDrain: Boolean): FluidStack = base.drain(resource, doDrain)
  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = base.drain(maxDrain, doDrain)
  override def fill(resource: FluidStack, doFill: Boolean): Int = base.fill(resource, doFill)
}
