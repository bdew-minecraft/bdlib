package net.bdew.lib.rich

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class RichWorld(val v: Level) extends AnyVal {
  def changeBlockState(pos: BlockPos, flags: Int)(f: BlockState => BlockState): Unit = {
    v.setBlock(pos, f(v.getBlockState(pos)), flags)
  }
}
