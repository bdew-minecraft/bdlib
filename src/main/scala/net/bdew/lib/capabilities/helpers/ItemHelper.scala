/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.helpers

import net.bdew.lib.capabilities.Capabilities.CAP_ITEM_HANDLER
import net.bdew.lib.capabilities.adapters.InventoryAdapter
import net.bdew.lib.capabilities.{CapAdapters, Capabilities}
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler

object ItemHelper {
  CapAdapters.add(Capabilities.CAP_ITEM_HANDLER, InventoryAdapter)

  def hasItemHandler(world: World, pos: BlockPos, side: Direction): Boolean =
    getItemHandler(world, pos, side).isDefined

  def hasItemHandler(stack: ItemStack): Boolean =
    getItemHandler(stack).isDefined

  def getItemHandler(world: World, pos: BlockPos, side: Direction): Option[IItemHandler] = {
    val tile = world.getBlockEntity(pos)
    if (tile == null) return None
    val cap = tile.getCapability(CAP_ITEM_HANDLER, side)
    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      CapAdapters.get(CAP_ITEM_HANDLER).wrap(tile, side)
  }

  def getItemHandler(stack: ItemStack): Option[IItemHandler] = {
    if (stack.isEmpty) return None
    val cap = stack.getCapability(CAP_ITEM_HANDLER, null)

    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      CapAdapters.get(CAP_ITEM_HANDLER).wrap(stack)
  }

  def pushItems(from: IItemHandler, to: IItemHandler, maxItems: Int = Int.MaxValue, filter: ItemStack => Boolean = !_.isEmpty): Unit = {
    var itemsPushed = 0
    for {
      fromSlot <- 0 until from.getSlots if filter(from.getStackInSlot(fromSlot))
      toSlot <- 0 until to.getSlots
    } {
      val canExtract = from.extractItem(fromSlot, maxItems - itemsPushed, true)
      if (!canExtract.isEmpty) {
        val leftAfterInsert = to.insertItem(toSlot, canExtract, false)
        if (leftAfterInsert == null) {
          from.extractItem(fromSlot, canExtract.getCount, false)
          itemsPushed += canExtract.getCount
        } else {
          from.extractItem(fromSlot, canExtract.getCount - leftAfterInsert.getCount, false)
          itemsPushed += canExtract.getCount - leftAfterInsert.getCount
        }
        if (itemsPushed >= maxItems) return
      }
    }
  }
}
