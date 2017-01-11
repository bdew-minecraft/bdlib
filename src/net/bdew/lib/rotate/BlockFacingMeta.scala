/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BaseBlock
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

/**
  * Mixin that stores rotation in metadata
  */
trait BlockFacingMeta extends BaseBlock with BaseRotatableBlock {
  setDefaultBlockState(getDefaultState.withProperty(Properties.FACING, getDefaultFacing))

  override def getProperties = super.getProperties :+ Properties.FACING

  override def setFacing(world: World, pos: BlockPos, facing: EnumFacing): Unit =
    world.changeBlockState(pos, 3) {
      _.withProperty(Properties.FACING, facing)
    }

  def getFacing(state: IBlockState): EnumFacing = state.getValue(Properties.FACING)

  override def getFacing(world: IBlockAccess, pos: BlockPos): EnumFacing =
    getFacing(world.getBlockState(pos))

  override def getStateFromMeta(meta: Int) =
    getDefaultState.withProperty(Properties.FACING, EnumFacing.getFront(meta))

  override def getMetaFromState(state: IBlockState) = {
    state.getValue(Properties.FACING).ordinal()
  }
}
