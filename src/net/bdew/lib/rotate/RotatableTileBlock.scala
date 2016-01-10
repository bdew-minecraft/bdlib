/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import net.bdew.lib.block.HasTE
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}

/**
  * Stores rotation data in tile entity. The TE should implement [[RotatableTile]]
  */
trait RotatableTileBlock extends BaseRotatableBlock with ITileEntityProvider {
  self: HasTE[_ <: RotatableTile] =>

  override def setFacing(world: World, pos: BlockPos, facing: EnumFacing) = {
    getTE(world, pos).rotation := facing
  }

  override def getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos) =
    state.withProperty(facingProperty, getTE(worldIn, pos).rotation.value)
}