package net.bdew.lib.multiblock.block

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockDisplayReader

class ColorHandler(resources: ResourceProvider) extends IBlockColor {
  override def getColor(state: BlockState, world: IBlockDisplayReader, pos: BlockPos, tintIndex: Int): Int =
    resources.outputColors.getOrElse(tintIndex, Color.white).asRGB
}
