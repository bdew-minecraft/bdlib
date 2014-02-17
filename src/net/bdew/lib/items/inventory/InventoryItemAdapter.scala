/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.items.inventory

import net.minecraft.item.ItemStack
import net.bdew.lib.tile.inventory.BaseInventory
import net.bdew.lib.Misc
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import net.minecraft.entity.player.EntityPlayer

class InventoryItemAdapter(val player: EntityPlayer, val slot: Int, val size: Int, val tagName: String) extends BaseInventory {
  def getSizeInventory = size
  def getStack = player.inventory.getStackInSlot(slot)

  if (getStack.hasTagCompound)
    load()

  def load() {
    val tag = getStack.getTagCompound
    for (nbtItem <- Misc.iterNbtList[NBTTagCompound](tag.getTagList(tagName))) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.size) {
        inv(slot) = ItemStack.loadItemStackFromNBT(nbtItem)
      }
    }
  }

  def onInventoryChanged() {
    if (!getStack.hasTagCompound) getStack.setTagCompound(new NBTTagCompound)
    val tag = getStack.getTagCompound
    val itemList = new NBTTagList
    for ((item, i) <- inv.view.zipWithIndex if item != null) {
      val itemNbt: NBTTagCompound = new NBTTagCompound
      itemNbt.setByte("Slot", i.asInstanceOf[Byte])
      item.writeToNBT(itemNbt)
      itemList.appendTag(itemNbt)
    }
    tag.setTag(tagName, itemList)
  }
}
