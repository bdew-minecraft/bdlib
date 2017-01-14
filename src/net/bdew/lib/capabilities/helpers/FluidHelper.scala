/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.helpers

import net.bdew.lib.capabilities.CapAdapters
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidHandlerItem}

object FluidHelper {

  import net.bdew.lib.capabilities.Capabilities.{CAP_FLUID_HANDLER, CAP_FLUID_HANDLER_ITEM}

  def hasFluidHandler(world: World, pos: BlockPos, side: EnumFacing): Boolean = {
    val tile = world.getTileEntity(pos)
    if (tile == null)
      false
    else if (tile.hasCapability(CAP_FLUID_HANDLER, side))
      true
    else CapAdapters.get(CAP_FLUID_HANDLER).canWrap(tile, side)
  }

  def hasFluidHandler(stack: ItemStack): Boolean = {
    if (stack.isEmpty)
      false
    else if (stack.hasCapability(CAP_FLUID_HANDLER_ITEM, null))
      true
    else CapAdapters.get(CAP_FLUID_HANDLER).canWrap(stack)
  }

  def getFluidHandler(world: World, pos: BlockPos, side: EnumFacing): Option[IFluidHandler] = {
    val tile = world.getTileEntity(pos)
    if (tile == null) return None
    if (tile.hasCapability(CAP_FLUID_HANDLER, side)) {
      val cap = tile.getCapability(CAP_FLUID_HANDLER, side)
      if (cap != null) return Some(cap)
    }
    CapAdapters.get(CAP_FLUID_HANDLER).wrap(tile, side)
  }

  def getFluidHandler(stack: ItemStack): Option[IFluidHandlerItem] = {
    if (stack.isEmpty) return None
    if (stack.hasCapability(CAP_FLUID_HANDLER_ITEM, null)) {
      val cap = stack.getCapability(CAP_FLUID_HANDLER_ITEM, null)
      if (cap != null) return Some(cap)
    }
    CapAdapters.get(CAP_FLUID_HANDLER_ITEM).wrap(stack)
  }

  /**
    * Attempt to move fluid between 2 handlers
    *
    * @param from   source handler
    * @param to     destination handler
    * @param doPush if false transfer will be only simulated
    * @param max    max amount of fluid to move
    * @return stack of moved fluid or null if not possible
    */
  def pushFluid(from: IFluidHandler, to: IFluidHandler, doPush: Boolean = true, max: Int = Integer.MAX_VALUE): FluidStack = {
    val drained = from.drain(max, false)
    if (drained != null) {
      val filled = to.fill(drained.copy(), doPush)
      if (filled > 0) {
        if (doPush)
          from.drain(filled, true)
        val tmp = drained.copy()
        tmp.amount = filled
        tmp
      } else null
    } else null
  }
}
