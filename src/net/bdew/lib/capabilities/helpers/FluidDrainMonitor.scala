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
import net.minecraftforge.fluids.capability.IFluidHandler

/**
  * Wraps another fluid handler and calls the passed onDrain function when fluid is drained
  */
class FluidDrainMonitor(val base: IFluidHandler, onDrain: FluidStack => Unit) extends FluidHandlerProxy {
  override def drain(resource: FluidStack, doDrain: Boolean): FluidStack = {
    val tmp = super.drain(resource, doDrain)
    if (tmp != null && tmp.amount > 0 && doDrain) onDrain(tmp)
    tmp
  }

  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = {
    val tmp = super.drain(maxDrain, doDrain)
    if (tmp != null && tmp.amount > 0 && doDrain) onDrain(tmp)
    tmp
  }
}
