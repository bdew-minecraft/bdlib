/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.machine

import net.bdew.lib.Misc
import net.bdew.lib.block.{BaseBlock, HasItemBlock, HasTE}
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.GameRegistry

abstract class Machine[T <: BaseBlock](val name: String, blockConstruct: => T) {
  var block: T = null.asInstanceOf[T]
  var tuning: ConfigSection = null
  var enabled = false
  val modId = Misc.getActiveModId

  def regBlock(creativeTab: CreativeTabs) {
    block = blockConstruct

    val itemClass: Class[_ <: ItemBlock] = if (block.isInstanceOf[HasItemBlock])
      block.asInstanceOf[HasItemBlock].ItemBlockClass
    else
      classOf[ItemBlock]

    block.preRegistration()

    GameRegistry.registerBlock(block, itemClass, name)

    if (FMLCommonHandler.instance().getSide.isClient) {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName, "inventory"))
    }

    block.setCreativeTab(creativeTab)
    if (block.isInstanceOf[HasTE[_]]) {
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass, "%s.%s".format(modId, name))
    }

  }
}
