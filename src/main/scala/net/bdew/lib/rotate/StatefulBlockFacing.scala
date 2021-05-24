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
import net.bdew.lib.block.StatefulBlock
import net.minecraft.block.{Block, BlockState}
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{Direction, Rotation}
import net.minecraft.world.{IBlockReader, World}

/**
 * Mixin that stores rotation in metadata
 */
trait StatefulBlockFacing extends StatefulBlock with BaseRotatableBlock {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.FACING, getDefaultFacing)

  override def createBlockStateDefinition(builder: StateContainer.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.FACING)
  }

  override def setFacing(world: World, pos: BlockPos, facing: Direction): Unit =
    world.changeBlockState(pos, 3) {
      _.setValue(BlockStateProperties.FACING, facing)
    }

  def getFacing(state: BlockState): Direction = state.getValue(BlockStateProperties.FACING)

  override def getFacing(world: IBlockReader, pos: BlockPos): Direction =
    getFacing(world.getBlockState(pos))

  override def rotate(state: BlockState, rot: Rotation): BlockState = {
    val newFacing = rot.rotate(getFacing(state))
    if (getValidFacings.contains(newFacing)) {
      state.setValue(BlockStateProperties.FACING, newFacing)
    } else {
      state
    }
  }
}
