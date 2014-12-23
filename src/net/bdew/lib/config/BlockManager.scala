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
import net.bdew.lib.Misc
import net.bdew.lib.block.{HasTE, NamedBlock}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

class BlockManager(creativeTab: CreativeTabs) {
  def regBlock[T <: NamedBlock](block: T): T = regBlock(block, block.name)

  def regBlock[T <: Block](block: T, name: String, addStack: Boolean = false): T = {
    GameRegistry.registerBlock(block, name)

    block.setCreativeTab(creativeTab)

    if (addStack)
      GameRegistry.registerCustomItemStack(name, new ItemStack(block))

    if (block.isInstanceOf[HasTE[_]])
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass,
        "%s.%s".format(Misc.getActiveModId, name))

    return block
  }

  def load() {}
}
