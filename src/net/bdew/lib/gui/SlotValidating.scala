/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class SlotValidating(inv: IInventory, slot: Int, x: Int, y: Int) extends BaseSlot(inv, slot, x, y) {
  override def isItemValid(stack: ItemStack) = inventory.isItemValidForSlot(getSlotIndex, stack)
}