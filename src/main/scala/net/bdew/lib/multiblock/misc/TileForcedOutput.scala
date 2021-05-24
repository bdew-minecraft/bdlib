/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.misc

import net.bdew.lib.block.WrenchableBlock
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.{TileModule, TileOutput}
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Direction, Hand}
import net.minecraft.world.World

trait TileForcedOutput extends TileModule {
  this: TileOutput[_] =>

  val forcedSides: Map[Direction, DataSlotBoolean] = Direction.values().map(f => f -> DataSlotBoolean("forced_" + f.name(), this, false).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE)).toMap

  override def coreRemoved(): Unit = {
    super.coreRemoved()
    forcedSides.values.foreach(_ := false)
  }

  def switchSideForced(face: Direction): Boolean = {
    if (!getLevel.isClientSide && getCore.isDefined) {
      forcedSides(face) := !forcedSides(face)
      doRescanFaces()
      true
    } else false
  }
}

trait BlockForcedOutput extends Block with WrenchableBlock {
  this: BlockModule[_ <: TileForcedOutput] =>
  override def wrenched(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, stack: ItemStack, hit: BlockRayTraceResult): ActionResultType = {
    if (world.isClientSide) return ActionResultType.SUCCESS
    getTE(world, pos).switchSideForced(hit.getDirection)
    ActionResultType.CONSUME
  }
}