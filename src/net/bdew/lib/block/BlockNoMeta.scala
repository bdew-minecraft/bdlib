/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraft.block.state.IBlockState

trait BlockNoMeta extends BaseBlock {
  override def getMetaFromState(state: IBlockState): Int = 0
  override def getStateFromMeta(meta: Int): IBlockState = getDefaultState
}
