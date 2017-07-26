/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.machine

import net.bdew.lib.Misc
import net.bdew.lib.block.BaseBlock
import net.bdew.lib.config.BlockManager
import net.bdew.lib.recipes.gencfg.ConfigSection

abstract class Machine[T <: BaseBlock](val name: String, blockConstruct: => T) {
  var block: T = null.asInstanceOf[T]
  var tuning: ConfigSection = _
  var enabled = false
  val modId = Misc.getActiveModId

  def regBlock(blocks: BlockManager) {
    block = blockConstruct
    blocks.regBlock(block)
  }
}
