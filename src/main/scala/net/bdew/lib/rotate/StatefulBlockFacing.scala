package net.bdew.lib.rotate

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.StatefulBlock
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.level.block.{Block, Rotation}
import net.minecraft.world.level.block.state.{BlockState, StateDefinition}
import net.minecraft.world.level.block.state.properties.BlockStateProperties

/**
 * Mixin that stores rotation in metadata
 */
trait StatefulBlockFacing extends StatefulBlock with BaseRotatableBlock {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.FACING, getDefaultFacing)

  override def createBlockStateDefinition(builder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.FACING)
  }

  override def setFacing(world: Level, pos: BlockPos, facing: Direction): Unit =
    world.changeBlockState(pos, 3) {
      _.setValue(BlockStateProperties.FACING, facing)
    }

  def getFacing(state: BlockState): Direction = state.getValue(BlockStateProperties.FACING)

  override def getFacing(world: BlockGetter, pos: BlockPos): Direction =
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
