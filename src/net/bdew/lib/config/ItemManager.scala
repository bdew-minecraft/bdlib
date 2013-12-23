/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.config

import net.minecraft.item.{ItemStack, Item}
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.lib.items.SimpleItem

class ItemManager(val ids: IdManager) {
  def regSimpleItem(name: String): SimpleItem = regItem(new SimpleItem(ids.getItemId(name), name), name)

  def regItem[T <: SimpleItem](item: T): T = regItem[T](item, item.name)

  def regItemCls[T <: Item](itemCls: Class[T], name: String, addStack: Boolean = true): T = {
    val item = itemCls.getConstructor(classOf[Int]).newInstance(ids.getItemId(name): Integer)
    regItem[T](item, name, addStack)
  }

  def regItem[T <: Item](item: T, name: String, addStack: Boolean = true): T = {
    GameRegistry.registerItem(item, name)
    if (addStack)
      GameRegistry.registerCustomItemStack(name, new ItemStack(item))
    return item
  }
  def load() {}
}
