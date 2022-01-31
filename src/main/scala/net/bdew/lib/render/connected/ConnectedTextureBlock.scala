package net.bdew.lib.render.connected

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block

trait ConnectedTextureBlock extends Block {
  def canConnect(world: BlockGetter, origin: BlockPos, target: BlockPos): Boolean
}
