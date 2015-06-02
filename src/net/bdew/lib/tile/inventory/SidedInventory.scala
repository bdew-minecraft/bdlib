/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack

trait SidedInventory extends BaseInventory with ISidedInventory {
  var allowSided = false

  def getAccessibleSlotsFromSide(side: Int): Array[Int] = inv.indices.toArray

  def canInsertItem(slot: Int, stack: ItemStack, side: Int) = allowSided && isItemValidForSlot(slot, stack)

  def canExtractItem(slot: Int, stack: ItemStack, side: Int) = allowSided
}
