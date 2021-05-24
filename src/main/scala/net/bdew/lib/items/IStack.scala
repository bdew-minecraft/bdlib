package net.bdew.lib.items

import net.minecraft.block.Block
import net.minecraft.item.{BlockItem, Item, ItemStack}

/**
 * Allows matching Items in ItemStacks in scala pattern matches
 */
object IStack {
  def unapply(x: ItemStack): Option[Item] =
    if (x.isEmpty)
      None
    else
      Some(x.getItem)
}

/**
 * Allows matching Blocks in ItemStacks in scala pattern matches
 */
object IStackBlock {
  def unapply(x: ItemStack): Option[Block] =
    if (x.isEmpty || !x.getItem.isInstanceOf[BlockItem])
      None
    else
      Some(x.getItem.asInstanceOf[BlockItem].getBlock)
}
