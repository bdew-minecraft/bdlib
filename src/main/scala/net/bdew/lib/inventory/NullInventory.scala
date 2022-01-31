package net.bdew.lib.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class NullInventory extends Container {
  override def getContainerSize: Int = 0
  override def isEmpty: Boolean = true
  override def getItem(slot: Int): ItemStack = ItemStack.EMPTY
  override def removeItem(slot: Int, amount: Int): ItemStack = ItemStack.EMPTY
  override def removeItemNoUpdate(slot: Int): ItemStack = ItemStack.EMPTY
  override def setItem(slot: Int, stack: ItemStack): Unit = {}
  override def setChanged(): Unit = {}
  override def stillValid(player: Player): Boolean = false
  override def clearContent(): Unit = {}
}

object NullInventory extends NullInventory