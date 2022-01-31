package net.bdew.lib.container

import net.bdew.lib.items.ItemUtils
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

abstract class BaseContainer(te: Container, containerType: MenuType[_], id: Int) extends NoInvContainer(containerType, id) {
  override def quickMoveStack(player: Player, slotNum: Int): ItemStack = {
    val slot = getSlot(slotNum)
    var stack = slot.getItem
    if (getSlot(slotNum).container == player.getInventory) {
      stack = ItemUtils.addStackToSlots(stack, te, 0 until te.getContainerSize, true)
    } else {
      stack = ItemUtils.addStackToSlots(stack, player.getInventory, 9 until player.getInventory.items.size(), true)
      if (!stack.isEmpty)
        stack = ItemUtils.addStackToSlots(stack, player.getInventory, 0 until 9, true)
    }
    slot.set(stack)
    slot.setChanged()
    ItemStack.EMPTY
  }

  override def stillValid(player: Player): Boolean = te.stillValid(player)
}

