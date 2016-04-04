/*
 * Copyright (c) bdew, 2013 - 2016
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
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{FMLCommonHandler, ObfuscationReflectionHelper}

class BlockManager(creativeTab: CreativeTabs) {
  def regBlock[T <: Block](block: T, skipTileEntityReg: Boolean = false): T = {
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
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass,
        "%s.%s".format(block.getRegistryName.getResourceDomain, block.getRegistryName.getResourcePath))

    if (FMLCommonHandler.instance().getSide.isClient && block.isInstanceOf[BaseBlockMixin]) {
      block.asInstanceOf[BaseBlockMixin].registerItemModels()
    }

    return block
  }

  /**
    * Registers a legacy TE name->class mapping. Stolen from GameRegistry.registerTileEntityWithAlternatives
    */
  def registerLegacyTileEntity(name: String, cls: Class[_ <: TileEntity]): Unit = {
    val teMappings: java.util.Map[String, Class[_]] = ObfuscationReflectionHelper.getPrivateValue(classOf[TileEntity], null, "field_" + "145855_i", "nameToClassMap")
    if (!teMappings.containsKey(name))
      teMappings.put(name, cls)
  }

  def load() {}
}
