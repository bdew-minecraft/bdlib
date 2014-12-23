/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.lib.items.{NamedItem, SimpleItem}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}

class ItemManager(creativeTab: CreativeTabs) {
  def regSimpleItem(name: String): SimpleItem = regItem(new SimpleItem(name), name)

  def regItem[T <: NamedItem](item: T): T = regItem[T](item, item.name)

  def regItem[T <: Item](item: T, name: String, addStack: Boolean = false): T = {
    GameRegistry.registerItem(item, name)
    item.setCreativeTab(creativeTab)
    if (addStack)
      GameRegistry.registerCustomItemStack(name, new ItemStack(item))
    return item
  }

  def load() {}
}
