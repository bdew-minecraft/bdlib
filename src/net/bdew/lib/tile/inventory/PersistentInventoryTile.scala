/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.bdew.lib.Misc
import net.bdew.lib.tile.TileExtended
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

trait PersistentInventoryTile extends TileExtended with BaseInventory {

  import net.bdew.lib.nbt._

  persistLoad.listen((tag: NBTTagCompound) => {
    inv = new Array[ItemStack](getSizeInventory)
    for (nbtItem <- tag.getList[NBTTagCompound]("Items")) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.length) {
        inv(slot) = ItemStack.loadItemStackFromNBT(nbtItem)
      }
    }
  })

  persistSave.listen((tag: NBTTagCompound) => {
    tag.setList("Items",
      for ((item, i) <- inv.view.zipWithIndex if item != null)
        yield Misc.applyMutator(new NBTTagCompound) { itemNbt =>
          itemNbt.setByte("Slot", i.asInstanceOf[Byte])
          item.writeToNBT(itemNbt)
        })
  })
}
