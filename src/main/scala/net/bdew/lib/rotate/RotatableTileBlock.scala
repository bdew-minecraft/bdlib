package net.bdew.lib.rotate

import net.bdew.lib.block.HasTE
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.{BlockGetter, Level}

/**
 * Stores rotation data in tile entity. The TE should implement [[RotatableTile]]
 */
trait RotatableTileBlock extends BaseRotatableBlock with EntityBlock {
  self: HasTE[_ <: RotatableTile] =>

  override def setFacing(world: Level, pos: BlockPos, facing: Direction): Unit = {
    getTE(world, pos).rotation := facing
  }

  override def getFacing(world: BlockGetter, pos: BlockPos): Direction =
    getTE(world, pos).map(_.rotation.value).getOrElse(getDefaultFacing)
}