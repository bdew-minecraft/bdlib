package net.bdew.lib.tile

import net.bdew.lib.block.HasTE
import net.minecraft.block.{Block, BlockState}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

trait BreakListeningTile extends TileEntity {
  def onBlockBroken(): Unit
}

trait BreakBroadcastingBlock extends Block {
  this: HasTE[_ <: BreakListeningTile] =>

  override def onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    if (!world.isClientSide) {
      getTE(world, pos).onBlockBroken()
    }
    super.onRemove(state, world, pos, newState, moving)
  }
}
