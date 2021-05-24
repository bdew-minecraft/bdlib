package net.bdew.lib.render.connected

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

trait ConnectedTextureBlock extends Block {
  def canConnect(world: IBlockReader, origin: BlockPos, target: BlockPos): Boolean
}
