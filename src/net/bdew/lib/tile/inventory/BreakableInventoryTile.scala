/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.bdew.lib.items.ItemUtils
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

trait BreakableInventoryTile extends TileEntity with BaseInventory {
  def dropItems() {
    if (getWorldObj != null && !getWorldObj.isRemote) {
      for (stack <- inv if stack != null) {
        ItemUtils.throwItemAt(getWorldObj, xCoord, yCoord, zCoord, stack)
      }
      inv = new Array[ItemStack](inv.length)
    }
  }
}
