/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

class ColorHandler(resources: ResourceProvider) extends IBlockColor {
  override def colorMultiplier(state: IBlockState, world: IBlockAccess, pos: BlockPos, tintIndex: Int): Int =
    resources.outputColors.getOrElse(tintIndex, Color.white).asRGB
}
