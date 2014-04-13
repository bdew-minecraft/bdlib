/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.items

import net.minecraft.item.{Item, ItemStack}

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
