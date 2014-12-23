/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.items.ItemUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

abstract class BaseContainer(te: IInventory) extends NoInvContainer {
  override def transferStackInSlot(player: EntityPlayer, slot: Int): ItemStack = {
    var stack = getSlot(slot).getStack
    if (getSlot(slot).inventory == player.inventory) {
      stack = ItemUtils.addStackToSlots(stack, te, 0 until te.getSizeInventory, true)
    } else {
      stack = ItemUtils.addStackToSlots(stack, player.inventory, 9 until player.inventory.mainInventory.length, true)
      // Only put in hotbar if the rest is full
      if (stack != null)
        stack = ItemUtils.addStackToSlots(stack, player.inventory, 0 until 9, true)
    }
    getSlot(slot).putStack(stack)
    return null
  }

  override def canInteractWith(player: EntityPlayer) = te.isUseableByPlayer(player)
}

