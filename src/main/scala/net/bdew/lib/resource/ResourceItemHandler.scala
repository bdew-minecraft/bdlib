package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

class ResourceItemHandler(slot: DataSlotResource) extends IItemHandler {
  override def getSlots: Int = 1
  override def getSlotLimit(n: Int): Int = 64

  override def isItemValid(n: Int, stack: ItemStack): Boolean =
    slot.canAccept(Resource.from(stack))

  override def getStackInSlot(n: Int): ItemStack = extractItem(n, Int.MaxValue, true)

  override def insertItem(n: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
    if (!isItemValid(n, stack)) return stack
    val filled = slot.fill(Resource.from(stack), true, !simulate)
    val remainder = stack.copy()
    if (filled > 0) remainder.shrink(filled.toInt)
    remainder
  }

  override def extractItem(n: Int, amount: Int, simulate: Boolean): ItemStack = {
    (for {
      res <- slot.drain(amount, true, false)
      rKind <- Misc.asInstanceOpt(res.kind, classOf[ItemResource])
    } yield {
      val stack = new ItemStack(rKind.item)
      stack.setCount(Misc.clamp(res.amount.toInt, 1, stack.getMaxStackSize))
      if (!simulate) slot.drain(stack.getCount, true, true)
      stack
    }) getOrElse ItemStack.EMPTY
  }
}
