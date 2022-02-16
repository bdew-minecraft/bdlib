package net.bdew.lib.container

import net.bdew.lib.Misc
import net.bdew.lib.items.ItemUtils
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

import scala.jdk.CollectionConverters._

abstract class BaseContainer(te: Container, containerType: MenuType[_], id: Int) extends NoInvContainer(containerType, id) {
  override def quickMoveStack(player: Player, slotNum: Int): ItemStack = {
    val sourceSlot = getSlot(slotNum)
    var sourceStack = sourceSlot.getItem

    if (getSlot(slotNum).container == player.getInventory) {
      for (target <- slots.asScala if !sourceStack.isEmpty && target.isActive && !sourceSlot.isSameInventory(target) && target.mayPlace(sourceStack) && ItemStack.isSameItemSameTags(sourceStack, target.getItem)) {
        val targetStack = target.getItem
        val toAdd = Misc.min(target.getMaxStackSize - targetStack.getCount, target.container.getMaxStackSize - targetStack.getCount, sourceStack.getCount)
        if (toAdd > 0) {
          targetStack.grow(toAdd)
          sourceStack.shrink(toAdd)
          target.set(targetStack)
        }
      }
      for (target <- slots.asScala if !sourceStack.isEmpty && target.isActive && !sourceSlot.isSameInventory(target) && target.mayPlace(sourceStack) && target.getItem.isEmpty) {
        val toAdd = Misc.min(target.getMaxStackSize, target.container.getMaxStackSize, sourceStack.getCount)
        if (toAdd > 0)
          target.set(sourceStack.split(toAdd))
      }
    } else {
      sourceStack = ItemUtils.addStackToSlots(sourceStack, player.getInventory, 9 until player.getInventory.items.size(), true)
      if (!sourceStack.isEmpty)
        sourceStack = ItemUtils.addStackToSlots(sourceStack, player.getInventory, 0 until 9, true)
    }
    sourceSlot.set(sourceStack)
    ItemStack.EMPTY
  }

  override def stillValid(player: Player): Boolean = te.stillValid(player)
}

