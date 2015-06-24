/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

/**
 * If defined on a block - defines a custom ItemBlock class that will be used when registering
 * Supported by BlockManager and Machine
 */
trait HasItemBlock extends Block {
  def ItemBlockClass: Class[_ <: ItemBlock]
}
