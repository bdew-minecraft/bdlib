/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.machine

import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraft.block.Block
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.item.ItemStack
import net.bdew.lib.block.HasTE
import net.bdew.lib.config.IdManager
import net.bdew.lib.Misc

abstract class Machine[T <: Block](val name: String, val blockConstruct: (Int) => T) {
  var block: T = null.asInstanceOf[T]
  var tuning: ConfigSection = null
  var enabled = false
  val modId = Misc.getActiveModId

  def regBlock(ids: IdManager) {
    block = blockConstruct(ids.getBlockId(name))
    GameRegistry.registerBlock(block, name)
    GameRegistry.registerCustomItemStack(name, new ItemStack(block))
    if (block.isInstanceOf[HasTE[_]]) {
      GameRegistry.registerTileEntity(block.asInstanceOf[HasTE[_]].TEClass, "%s.%s".format(modId, name))
    }

  }
}
