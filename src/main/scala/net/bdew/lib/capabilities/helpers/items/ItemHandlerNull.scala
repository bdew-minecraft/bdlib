package net.bdew.lib.capabilities.helpers.items

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

object ItemHandlerNull extends IItemHandler {
  override def getSlots: Int = 0
  override def getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY
  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = stack
  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
  override def getSlotLimit(slot: Int): Int = 0
  override def isItemValid(slot: Int, stack: ItemStack): Boolean = false
}
