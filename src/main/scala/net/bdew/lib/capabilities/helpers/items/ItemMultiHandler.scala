package net.bdew.lib.capabilities.helpers.items

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

abstract class ItemMultiHandler extends IItemHandler {
  def handlers: List[IItemHandler]
  def mapSlots: List[(IItemHandler, Int)]

  override def getSlots: Int = handlers.map(_.getSlots).sum

  private def run[T](slot: Int, f: (IItemHandler, Int) => T): Option[T] =
    mapSlots.lift(slot).map(x => f.tupled(x))

  override def getSlotLimit(slot: Int): Int = run(slot, _.getSlotLimit(_)).getOrElse(0)

  override def getStackInSlot(slot: Int): ItemStack =
    run(slot, _.getStackInSlot(_)).getOrElse(ItemStack.EMPTY)

  override def isItemValid(slot: Int, stack: ItemStack): Boolean =
    run(slot, _.isItemValid(_, stack)).getOrElse(false)

  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack =
    run(slot, _.insertItem(_, stack, simulate)).getOrElse(stack)

  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
    run(slot, _.extractItem(_, amount, simulate)).getOrElse(ItemStack.EMPTY)
}

class ItemMultiHandlerStatic(val handlers: List[IItemHandler]) extends ItemMultiHandler {
  override val mapSlots: List[(IItemHandler, Int)] = handlers.flatMap(x => (0 until x.getSlots).map(t => (x, t)))
}

class ItemMultiHandlerDynamic(getHandlers: () => List[IItemHandler]) extends ItemMultiHandler {
  override def handlers: List[IItemHandler] = getHandlers()
  override def mapSlots: List[(IItemHandler, Int)] = getHandlers().flatMap(x => (0 until x.getSlots).map(t => (x, t)))
}

object ItemMultiHandler {
  def wrap(handlers: List[IItemHandler]): IItemHandler = {
    if (handlers.length == 1)
      handlers.head
    else
      new ItemMultiHandlerStatic(handlers)
  }

  def wrap(handlers: IItemHandler*): IItemHandler = {
    if (handlers.length == 1)
      handlers.head
    else
      new ItemMultiHandlerStatic(handlers.toList)
  }

  def dynamic(getHandlers: () => List[IItemHandler]): IItemHandler = {
    new ItemMultiHandlerDynamic(getHandlers)
  }
}
