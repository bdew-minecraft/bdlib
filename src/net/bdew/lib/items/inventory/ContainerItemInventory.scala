/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.items.inventory

import net.bdew.lib.gui.BaseContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerItemInventory(inv: InventoryItemAdapter, player: EntityPlayer) extends BaseContainer(inv) {
  def canInteractWith(entityplayer: EntityPlayer) = entityplayer == player

  override def slotClick(slotnum: Int, button: Int, modifiers: Int, player: EntityPlayer): ItemStack = {
    if (slotnum > 0 && slotnum < inventorySlots.size()) {
      val slot = inventorySlots.asInstanceOf[java.util.List[Slot]].get(slotnum)
      if (slot.isSlotInInventory(inv.player.inventory, inv.slot))
        return null
    }
    if (modifiers == 2 && button == inv.slot) return null
    return super.slotClick(slotnum, button, modifiers, player)
  }
}
