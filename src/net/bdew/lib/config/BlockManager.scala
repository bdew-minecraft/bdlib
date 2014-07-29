/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.config

import cpw.mods.fml.common.registry.GameRegistry
import net.bdew.lib.Misc
import net.bdew.lib.block.HasTE
import net.minecraft.block.Block
import net.minecraft.item.ItemStack

class BlockManager() {

  def regBlockCls[T <: Block](blockCls: Class[T], name: String, addStack: Boolean = false): T = {
    val block = blockCls.newInstance()
    regBlock[T](block, name, addStack)
  }

  def regBlock[T <: Block](block: T, name: String, addStack: Boolean = false): T = {
    GameRegistry.registerBlock(block, name)

    if (addStack)
      GameRegistry.registerCustomItemStack(name, new ItemStack(block))

    if (block.isInstanceOf[HasTE[_]])
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass,
        "%s.%s".format(Misc.getActiveModId, name))

    return block
  }

  def load() {}
}
