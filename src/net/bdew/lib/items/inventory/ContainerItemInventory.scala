/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items.inventory

import net.bdew.lib.gui.BaseContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerItemInventory(inv: InventoryItemAdapter, player: EntityPlayer) extends BaseContainer(inv) {
  override def slotClick(slotNum: Int, button: Int, modifiers: Int, player: EntityPlayer): ItemStack = {
    if (slotNum > 0 && slotNum < inventorySlots.size()) {
      val slot = inventorySlots.asInstanceOf[java.util.List[Slot]].get(slotNum)
      if (slot.isSlotInInventory(inv.player.inventory, inv.slot))
        return null
    }
    if (modifiers == 2 && button == inv.slot) return null
    return super.slotClick(slotNum, button, modifiers, player)
  }
}
