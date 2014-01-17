/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.inventory.{Slot, Container}
import net.bdew.lib.Misc
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack

abstract class NoInvContainer extends Container {

  import scala.collection.JavaConversions._

  def players = Misc.filterType(crafters, classOf[EntityPlayerMP])

  protected def bindPlayerInventory(inv: InventoryPlayer, xOffs: Int, yOffsInv: Int, yOffsHotbar: Int) {
    for (i <- 0 until 3; j <- 0 until 9)
      addSlotToContainer(new Slot(inv, j + i * 9 + 9, xOffs + j * 18, yOffsInv + i * 18))

    for (i <- 0 until 9)
      addSlotToContainer(new Slot(inv, i, xOffs + i * 18, yOffsHotbar))
  }

  override def slotClick(slotnum: Int, button: Int, modifiers: Int, player: EntityPlayer): ItemStack = {
    if (inventorySlots.isDefinedAt(slotnum)) {
      val slot = getSlot(slotnum)
      if (slot != null && slot.isInstanceOf[SlotClickable])
        return slot.asInstanceOf[SlotClickable].onClick(button, modifiers, player)
    }
    return super.slotClick(slotnum, button, modifiers, player)
  }

  override def transferStackInSlot(player: EntityPlayer, slot: Int): ItemStack = null
}
