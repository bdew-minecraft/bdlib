/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.tile.inventory

import net.bdew.lib.Misc
import net.bdew.lib.tile.TileExtended
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

trait PersistentInventoryTile extends TileExtended with BaseInventory {
  persistLoad.listen((tag: NBTTagCompound) => {
    inv = new Array[ItemStack](getSizeInventory)
    for (nbtItem <- Misc.iterNbtCompoundList(tag, "Items")) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.size) {
        inv(slot) = ItemStack.loadItemStackFromNBT(nbtItem)
      }
    }
  })

  persistSave.listen((tag: NBTTagCompound) => {
    val itemList = new NBTTagList
    for ((item, i) <- inv.view.zipWithIndex if item != null) {
      val itemNbt: NBTTagCompound = new NBTTagCompound
      itemNbt.setByte("Slot", i.asInstanceOf[Byte])
      item.writeToNBT(itemNbt)
      itemList.appendTag(itemNbt)
    }
    tag.setTag("Items", itemList)
  })
}
