package net.bdew.lib.capabilities.helpers.items

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

class RestrictedItemHandler(val base: IItemHandler, canInsert: (Int, ItemStack) => Boolean, canExtract: Int => Boolean) extends ItemHandlerProxy {
  override def isItemValid(slot: Int, stack: ItemStack): Boolean = canInsert(slot, stack) && super.isItemValid(slot, stack)

  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack =
    if (canInsert(slot, stack))
      super.insertItem(slot, stack, simulate)
    else
      ItemStack.EMPTY

  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
    if (canExtract(slot))
      super.extractItem(slot, amount, simulate)
    else
      ItemStack.EMPTY
}

object RestrictedItemHandler {
  def insertOnly(base: IItemHandler) = new RestrictedItemHandler(base, canInsert = (_, _) => true, canExtract = _ => false)
  def extractOnly(base: IItemHandler) = new RestrictedItemHandler(base, canInsert = (_, _) => false, canExtract = _ => true)
  def closed(base: IItemHandler) = new RestrictedItemHandler(base, canInsert = (_, _) => false, canExtract = _ => false)
}