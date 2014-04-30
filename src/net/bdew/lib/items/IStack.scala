/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.items

import net.minecraft.item.{ItemBlock, Item, ItemStack}
import net.minecraft.block.Block

/**
 * Allows matching Items in ItemStacks in scala pattern matches
 */
object IStack {
  def unapply(x: ItemStack): Option[Item] =
    if (x == null || x.getItem == null)
      None
    else
      Some(x.getItem)
}

/**
 * Allows matching Blocks in ItemStacks in scala pattern matches
 */
object IStackBlock {
  def unapply(x: ItemStack): Option[Block] =
    if (x == null || x.getItem == null || !x.getItem.isInstanceOf[ItemBlock])
      None
    else
      Some(x.getItem.asInstanceOf[ItemBlock].field_150939_a)
}
