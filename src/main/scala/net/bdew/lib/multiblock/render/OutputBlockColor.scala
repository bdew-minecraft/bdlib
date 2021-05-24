package net.bdew.lib.multiblock.render

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockDisplayReader

class OutputBlockColor(resources: ResourceProvider) extends IBlockColor {
  override def getColor(state: BlockState, world: IBlockDisplayReader, pos: BlockPos, index: Int): Int =
    resources.outputColors.getOrElse(index - 1, Color.white).asARGB
}
