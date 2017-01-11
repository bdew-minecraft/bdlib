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
  * Empty FluidHandler that does nothing
  */
object FluidHandlerNull extends IFluidHandler {
  override def getTankProperties: Array[IFluidTankProperties] = Array.empty
  override def drain(resource: FluidStack, doDrain: Boolean): FluidStack = null
  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = null
  override def fill(resource: FluidStack, doFill: Boolean): Int = 0
}
