/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

trait SidedInventory extends BaseInventory with ISidedInventory {
  var allowSided = false

  override def getSlotsForFace(side: EnumFacing): Array[Int] = inv.indices.toArray

  override def canExtractItem(slot: Int, stack: ItemStack, direction: EnumFacing): Boolean = allowSided

  override def canInsertItem(slot: Int, stack: ItemStack, direction: EnumFacing): Boolean = allowSided && isItemValidForSlot(slot, stack)
}
