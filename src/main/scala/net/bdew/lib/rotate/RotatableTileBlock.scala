package net.bdew.lib.rotate

import net.bdew.lib.block.HasTE
import net.minecraft.block.ITileEntityProvider
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}

/**
 * Stores rotation data in tile entity. The TE should implement [[RotatableTile]]
 */
trait RotatableTileBlock extends BaseRotatableBlock with ITileEntityProvider {
  self: HasTE[_ <: RotatableTile] =>

  override def setFacing(world: World, pos: BlockPos, facing: Direction): Unit = {
    getTE(world, pos).rotation := facing
  }

  override def getFacing(world: IBlockReader, pos: BlockPos): Direction =
    getTE(world, pos).map(_.rotation.value).getOrElse(getDefaultFacing)
}