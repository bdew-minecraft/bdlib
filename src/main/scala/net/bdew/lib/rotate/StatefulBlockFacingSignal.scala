package net.bdew.lib.rotate

import net.bdew.lib.PimpVanilla._
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockState, StateDefinition}
import net.minecraft.world.level.block.state.properties.BlockStateProperties

/**
 * Mixin that stores rotation + boolean value in metadata
 */
trait StatefulBlockFacingSignal extends StatefulBlockFacing {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.POWERED, Boolean.box(false))

  override def createBlockStateDefinition(builder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.POWERED)
  }

  def getSignal(state: BlockState): Boolean = state.getValue(BlockStateProperties.POWERED)

  def getSignal(world: BlockGetter, pos: BlockPos): Boolean = getSignal(world.getBlockState(pos))

  def setSignal(world: Level, pos: BlockPos, signal: Boolean): Unit =
    world.changeBlockState(pos, 3) { state =>
      state.setValue(BlockStateProperties.POWERED, Boolean.box(signal))
    }

  override def canConnectRedstone(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Boolean = true
  override def isSignalSource(state: BlockState): Boolean = true
}
