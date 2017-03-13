/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.tile.inventory.BaseInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

case class DataSlotInventory(name: String, parent: DataSlotContainer, size: Int) extends DataSlot with BaseInventory {
  setUpdate(UpdateKind.SAVE)

  override def getSizeInventory = size
  override def markDirty() = parent.dataSlotChanged(this)

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    inv = Array.fill(getSizeInventory)(ItemStack.EMPTY)
    for (nbtItem <- t.getList[NBTTagCompound](name)) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.length) {
        inv(slot) = new ItemStack(nbtItem)
      }
    }
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    val itemList = new NBTTagList
    for ((item, i) <- inv.view.zipWithIndex if !item.isEmpty) {
      val itemNbt: NBTTagCompound = new NBTTagCompound
      itemNbt.setByte("Slot", i.asInstanceOf[Byte])
      item.writeToNBT(itemNbt)
      itemList.appendTag(itemNbt)
    }
    t.setTag(name, itemList)
  }

  override def isUsableByPlayer(player: EntityPlayer): Boolean = parent.isEntityInRange(player, 64D)
}


