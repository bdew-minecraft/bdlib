package net.bdew.lib.rotate

import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.{BlockGetter, Level}

import java.util

/**
 * Basic logic for block rotation
 */
trait BaseRotatableBlock extends Block {
  /**
   * Set of valid rotations, default is all of them
   */
  def getValidFacings: util.EnumSet[Direction] =
    util.EnumSet.allOf(classOf[Direction])

  /**
   * Rotation to show when it's unavailable (like rendering item in inventory)
   */
  def getDefaultFacing: Direction = Direction.UP

  /**
   * Those will be overridden to provide concrete storage
   */
  def setFacing(world: Level, pos: BlockPos, facing: Direction): Unit
  def getFacing(world: BlockGetter, pos: BlockPos): Direction

  override def setPlacedBy(world: Level, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    val dir = RotatedHelper.getFacingFromEntity(placer, getValidFacings, getDefaultFacing)
    setFacing(world, pos, dir)
    super.setPlacedBy(world, pos, state, placer, stack)
  }
}
