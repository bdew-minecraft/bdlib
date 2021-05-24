/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

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
