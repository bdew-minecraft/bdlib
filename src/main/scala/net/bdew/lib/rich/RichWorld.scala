package net.bdew.lib.rich

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class RichWorld(val v: World) extends AnyVal {
  def changeBlockState(pos: BlockPos, flags: Int)(f: BlockState => BlockState): Unit = {
    v.setBlock(pos, f(v.getBlockState(pos)), flags)
  }
}
