/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.container

import net.bdew.lib.items.ItemUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.ItemStack

abstract class BaseContainer(te: IInventory, containerType: ContainerType[_], id: Int) extends NoInvContainer(containerType, id) {
  override def quickMoveStack(player: PlayerEntity, slotNum: Int): ItemStack = {
    val slot = getSlot(slotNum)
    var stack = slot.getItem
    if (getSlot(slotNum).container == player.inventory) {
      stack = ItemUtils.addStackToSlots(stack, te, 0 until te.getContainerSize, true)
    } else {
      stack = ItemUtils.addStackToSlots(stack, player.inventory, 9 until player.inventory.items.size(), true)
      if (!stack.isEmpty)
        stack = ItemUtils.addStackToSlots(stack, player.inventory, 0 until 9, true)
    }
    slot.set(stack)
    slot.setChanged()
    ItemStack.EMPTY
  }

  override def stillValid(player: PlayerEntity): Boolean = te.stillValid(player)
}

