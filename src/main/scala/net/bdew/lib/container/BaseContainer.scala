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

