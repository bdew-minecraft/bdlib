/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.Misc
import net.bdew.lib.block.{HasItemBlock, HasTE, NamedBlock}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.common.registry.GameRegistry

class BlockManager(creativeTab: CreativeTabs) {
  def regBlock[T <: NamedBlock](block: T): T = regBlock(block, block.name)

  def regSpecial[T <: NamedBlock](block: T, skipTileEntityReg: Boolean = false): T =
    regBlock(block, block.name, skipTileEntityReg)

  def regBlock[T <: Block](block: T, name: String, skipTileEntityReg: Boolean = false): T = {
    val itemClass: Class[_ <: ItemBlock] = if (block.isInstanceOf[HasItemBlock])
      block.asInstanceOf[HasItemBlock].ItemBlockClass
    else
      classOf[ItemBlock]

    GameRegistry.registerBlock(block, itemClass, name)

    block.setCreativeTab(creativeTab)

    if (block.isInstanceOf[HasTE[_]] && !skipTileEntityReg)
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
