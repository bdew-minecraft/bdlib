/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.tile.inventory

import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import net.bdew.lib.items.ItemUtils

trait BreakableInventoryTile extends TileEntity with BaseInventory {
  def dropItems() {
    if (worldObj != null && !worldObj.isRemote) {
      for (stack <- inv if stack != null) {
        ItemUtils.throwItemAt(worldObj, xCoord, yCoord, zCoord, stack)
      }
      inv = new Array[ItemStack](inv.size)
    }
  }
}
