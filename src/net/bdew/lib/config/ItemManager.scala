/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.items.{BaseItem, BaseItemMixin}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.ForgeRegistries

class ItemManager(creativeTab: CreativeTabs) {

  def regSimpleItem(name: String): BaseItem = regItem(new BaseItem(name))

  def regItem[T <: Item](item: T): T = {
    require(item.getRegistryName != null, "Attempting to register item with null name")
    require(!item.getRegistryName.getResourcePath.exists(_.isUpper), "Block names should contain no upper case character")

    ForgeRegistries.ITEMS.register(item)
    item.setCreativeTab(creativeTab)

    if (FMLCommonHandler.instance().getSide.isClient && item.isInstanceOf[BaseItemMixin]) {
      item.asInstanceOf[BaseItemMixin].registerItemModels()
    }

    return item
  }

  def load() {}
}
