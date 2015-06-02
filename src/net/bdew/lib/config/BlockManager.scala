/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import cpw.mods.fml.common.ObfuscationReflectionHelper
import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.lib.Misc
import net.bdew.lib.block.{HasTE, NamedBlock}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity

class BlockManager(creativeTab: CreativeTabs) {
  def regBlock[T <: NamedBlock](block: T): T = regBlock(block, block.name)

  def regBlock[T <: NamedBlock](block: T, itemClass: Class[_ <: ItemBlock]): T = regBlock(block, block.name, itemClass)

  def regBlock[T <: Block](block: T, name: String, itemClass: Class[_ <: ItemBlock] = classOf[ItemBlock], addStack: Boolean = false): T = {
    GameRegistry.registerBlock(block, itemClass, name)

    block.setCreativeTab(creativeTab)

    if (addStack)
      GameRegistry.registerCustomItemStack(name, new ItemStack(block))

    if (block.isInstanceOf[HasTE[_]])
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass,
        "%s.%s".format(Misc.getActiveModId, name))

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
