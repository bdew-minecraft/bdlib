package net.bdew.lib.tile

import net.bdew.lib.block.HasTE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

trait BreakListeningTile extends BlockEntity {
  def onBlockBroken(): Unit
}

trait BreakBroadcastingBlock extends Block {
  this: HasTE[_ <: BreakListeningTile] =>

  override def onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    if (!world.isClientSide) {
      getTE(world, pos).onBlockBroken()
    }
    super.onRemove(state, world, pos, newState, moving)
  }
}
