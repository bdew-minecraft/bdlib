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
 * Empty FluidHandler that does nothing
 */
object FluidHandlerNull extends IFluidHandler {
  override def getTanks: Int = 0
  override def getFluidInTank(tank: Int): FluidStack = FluidStack.EMPTY
  override def getTankCapacity(tank: Int): Int = 0
  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = false
  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = 0
  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
}
