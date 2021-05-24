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
import net.bdew.lib.inventory.BaseInventory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompoundNBT, ListNBT}

case class DataSlotInventory(name: String, parent: DataSlotContainer, size: Int) extends DataSlot with BaseInventory {
  setUpdate(UpdateKind.SAVE, UpdateKind.UPDATE)

  override def getContainerSize: Int = size
  override def setChanged(): Unit = parent.dataSlotChanged(this)

  override def load(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    inv = Array.fill(size)(ItemStack.EMPTY)
    for (nbtItem <- t.getListVals[CompoundNBT](name)) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.length) {
        inv(slot) = ItemStack.of(nbtItem)
      }
    }
  }

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    val itemList = new ListNBT()
    for ((item, i) <- inv.view.zipWithIndex if !item.isEmpty) {
      val itemNbt: CompoundNBT = new CompoundNBT
      itemNbt.putByte("Slot", i.asInstanceOf[Byte])
      item.save(itemNbt)
      itemList.add(itemNbt)
    }
    t.setVal(name, itemList)
  }

  override def stillValid(player: PlayerEntity): Boolean = parent.isEntityInRange(player, 64D)
}


