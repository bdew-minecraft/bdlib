package net.bdew.lib.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class NullInventory extends IInventory {
  override def getContainerSize: Int = 0
  override def isEmpty: Boolean = true
  override def getItem(slot: Int): ItemStack = ItemStack.EMPTY
  override def removeItem(slot: Int, amount: Int): ItemStack = ItemStack.EMPTY
  override def removeItemNoUpdate(slot: Int): ItemStack = ItemStack.EMPTY
  override def setItem(slot: Int, stack: ItemStack): Unit = {}
  override def setChanged(): Unit = {}
  override def stillValid(player: PlayerEntity): Boolean = false
  override def clearContent(): Unit = {}
}

object NullInventory extends NullInventory