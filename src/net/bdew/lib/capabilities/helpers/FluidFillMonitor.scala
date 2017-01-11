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
  * Wraps another fluid handler and calls the passed onFill function when fluid is filled
  */
class FluidFillMonitor(val base: IFluidHandler, onFill: FluidStack => Unit) extends FluidHandlerProxy {
  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    val filled = super.fill(resource, doFill)
    if (filled > 0 && doFill) {
      val tmp = resource.copy()
      tmp.amount = filled
      onFill(tmp)
    }
    filled
  }
}
