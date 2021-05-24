package net.bdew.lib.capabilities.helpers.items

import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler

class ItemHandlerOptional(handler: () => Option[IItemHandler]) extends ItemHandlerProxy {
  override def base: IItemHandler = handler().getOrElse(ItemHandlerNull)
}

object ItemHandlerOptional {
  def create(base: () => Option[IItemHandler]): LazyOptional[IItemHandler] = {
    val handler = new ItemHandlerOptional(base)
    LazyOptional.of(() => handler)
  }
}
