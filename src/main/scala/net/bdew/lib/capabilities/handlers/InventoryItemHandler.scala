package net.bdew.lib.capabilities.handlers

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.wrapper.InvWrapper

class InventoryItemHandler(inv: IInventory, canExtract: Int => Boolean, canInsert: (Int, ItemStack) => Boolean) extends InvWrapper(inv) {
  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
    if (canInsert(slot, stack))
      super.insertItem(slot, stack, simulate)
    else
      ItemStack.EMPTY
  }

  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = {
    if (canExtract(slot))
      super.extractItem(slot, amount, simulate)
    else
      ItemStack.EMPTY
  }

  override def isItemValid(slot: Int, stack: ItemStack): Boolean =
    canInsert(slot, stack) && super.isItemValid(slot, stack)
}

object InventoryItemHandler {
  def create(inv: IInventory, canExtract: Int => Boolean, canInsert: (Int, ItemStack) => Boolean): LazyOptional[InventoryItemHandler] = {
    val handler = new InventoryItemHandler(inv, canExtract, canInsert)
    LazyOptional.of(() => handler)
  }
}
