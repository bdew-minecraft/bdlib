package net.bdew.lib.rotate

import net.bdew.lib.PimpVanilla._
import net.minecraft.block.{Block, BlockState}
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}

/**
 * Mixin that stores rotation + boolean value in metadata
 */
trait StatefulBlockFacingSignal extends StatefulBlockFacing {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.POWERED, Boolean.box(false))

  override def createBlockStateDefinition(builder: StateContainer.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.POWERED)
  }

  def getSignal(state: BlockState): Boolean = state.getValue(BlockStateProperties.POWERED)

  def getSignal(world: IBlockReader, pos: BlockPos): Boolean = getSignal(world.getBlockState(pos))

  def setSignal(world: World, pos: BlockPos, signal: Boolean): Unit =
    world.changeBlockState(pos, 3) { state =>
      state.setValue(BlockStateProperties.POWERED, Boolean.box(signal))
    }

  override def canConnectRedstone(state: BlockState, world: IBlockReader, pos: BlockPos, side: Direction): Boolean = true
  override def isSignalSource(state: BlockState): Boolean = true
}
