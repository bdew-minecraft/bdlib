package net.bdew.lib.multiblock.render

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.client.color.block.BlockColor
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState

class OutputBlockColor(resources: ResourceProvider) extends BlockColor {
  override def getColor(state: BlockState, world: BlockAndTintGetter, pos: BlockPos, index: Int): Int =
    resources.outputColors.getOrElse(index - 1, Color.white).asARGB
}
