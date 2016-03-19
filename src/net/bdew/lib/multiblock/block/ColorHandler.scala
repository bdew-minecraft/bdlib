package net.bdew.lib.multiblock.block

import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

class ColorHandler(resources: ResourceProvider) extends IBlockColor {
  override def colorMultiplier(state: IBlockState, world: IBlockAccess, pos: BlockPos, tintIndex: Int): Int =
    resources.outputColors.getOrElse(tintIndex, Color.white).asRGB
}
