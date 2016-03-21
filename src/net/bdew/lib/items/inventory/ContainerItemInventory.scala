/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items.inventory

import net.bdew.lib.gui.BaseContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ClickType
import net.minecraft.item.ItemStack

class ContainerItemInventory(inv: InventoryItemAdapter, player: EntityPlayer) extends BaseContainer(inv) {
  //slotClick
  override def slotClick(slotNum: Int, dragType: Int, clickType: ClickType, player: EntityPlayer): ItemStack = {
    if (slotNum > 0 && slotNum < inventorySlots.size()) {
      val slot = inventorySlots.get(slotNum)
      if (slot.isHere(inv.player.inventory, inv.slot))
        return null
    }
    // Fixme: update to new system
    //if (modifiers == 2 && button == inv.slot) return null
    super.slotClick(slotNum, dragType, clickType, player)
  }
}
