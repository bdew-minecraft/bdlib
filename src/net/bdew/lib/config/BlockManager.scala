/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.BdLib
import net.bdew.lib.block.{BaseBlockMixin, HasItemBlock, HasTE}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.{GameData, GameRegistry}

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

    GameRegistry.register(block)
    GameRegistry.register(itemBlock)

    block.setCreativeTab(creativeTab)

    if (block.isInstanceOf[HasTE[_]] && !skipTileEntityReg)
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass, block.getRegistryName.toString)

    if (FMLCommonHandler.instance().getSide.isClient && block.isInstanceOf[BaseBlockMixin]) {
      block.asInstanceOf[BaseBlockMixin].registerItemModels()
    }

    return block
  }

  /**
    * Registers a legacy TE name->class mapping. Stolen from GameRegistry.registerTileEntityWithAlternatives
    */
  def registerLegacyTileEntity(name: String, cls: Class[_ <: TileEntity]): Unit = {
    val newName = GameData.getTileEntityRegistry.getNameForObject(cls)
    if (newName == null) {
      BdLib.logWarn(s"Asked to register alternative name $name for TE class ${ cls.getName } that isn't registered!")
    } else {
      BdLib.logDebug(s"Registering legacy TE name $name -> $newName")
      GameData.getTileEntityRegistry.addLegacyName(new ResourceLocation(name), newName)
    }
  }

  def load() {}
}
