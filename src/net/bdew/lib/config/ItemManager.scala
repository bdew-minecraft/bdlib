/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.items.BaseItem
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.GameRegistry

class ItemManager(creativeTab: CreativeTabs) {
  def regSimpleItem(name: String): BaseItem = regItem(new BaseItem(name))

  def regItem[T <: BaseItem](item: T): T = {
    GameRegistry.registerItem(item, item.name)
    item.setCreativeTab(creativeTab)

    if (FMLCommonHandler.instance().getSide.isClient) {
      item.registerItemModels()
    }

    return item
  }

  def load() {}
}
