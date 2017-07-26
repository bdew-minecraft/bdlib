/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.block.{BaseBlockMixin, HasItemBlock, HasTE}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.{ForgeRegistries, GameRegistry}

class BlockManager(creativeTab: CreativeTabs) {
  def regBlock[T <: Block](block: T, skipTileEntityReg: Boolean = false): T = {
    require(block.getRegistryName != null, "Attempting to register block with null name")
    require(!block.getRegistryName.getResourcePath.exists(_.isUpper), "Block names should contain no upper case character")

    val itemBlock = if (block.isInstanceOf[HasItemBlock])
      block.asInstanceOf[HasItemBlock].itemBlockInstance
    else
      new ItemBlock(block).setRegistryName(block.getRegistryName)

    if (block.getRegistryName != itemBlock.getRegistryName)
      sys.error("Registry name mismatch between block/item for %s (%s)".format(block.getRegistryName, block.getClass.getName))

    ForgeRegistries.BLOCKS.register(block)
    ForgeRegistries.ITEMS.register(itemBlock)

    block.setCreativeTab(creativeTab)

    if (block.isInstanceOf[HasTE[_]] && !skipTileEntityReg)
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass, block.getRegistryName.toString)

    if (FMLCommonHandler.instance().getSide.isClient && block.isInstanceOf[BaseBlockMixin]) {
      block.asInstanceOf[BaseBlockMixin].registerItemModels()
    }

    return block
  }

  def load() {}
}
