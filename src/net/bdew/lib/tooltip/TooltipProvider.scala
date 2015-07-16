/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tooltip

import net.minecraft.item.ItemStack

trait TooltipProvider {
  def shouldHandleTooltip(stack: ItemStack): Boolean
  def handleTooltip(stack: ItemStack, advanced: Boolean, shift: Boolean): Iterable[String]
}

