package net.bdew.lib.capabilities.helpers.items

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandler

trait ItemHandlerProxy extends IItemHandler {
  def base: IItemHandler

  override def getSlots: Int = base.getSlots
  override def getSlotLimit(slot: Int): Int = base.getSlotLimit(slot)
  override def getStackInSlot(slot: Int): ItemStack = base.getStackInSlot(slot)
  override def isItemValid(slot: Int, stack: ItemStack): Boolean = base.isItemValid(slot, stack)
  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = base.insertItem(slot, stack, simulate)
  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = base.extractItem(slot, amount, simulate)
}
