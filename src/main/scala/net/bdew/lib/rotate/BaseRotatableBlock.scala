/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}

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
  def setFacing(world: World, pos: BlockPos, facing: Direction): Unit
  def getFacing(world: IBlockReader, pos: BlockPos): Direction

  override def setPlacedBy(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    val dir = RotatedHelper.getFacingFromEntity(placer, getValidFacings, getDefaultFacing)
    setFacing(world, pos, dir)
    super.setPlacedBy(world, pos, state, placer, stack)
  }
}
