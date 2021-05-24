/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.block.HasTE
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.{AbstractBlock, Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Hand}
import net.minecraft.world.{IBlockReader, World}

abstract class BlockController[T <: TileController](props: AbstractBlock.Properties)
  extends Block(props) with HasTE[T] with ConnectedTextureBlock {

  override def onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    getTE(world, pos).onBreak()
    super.onRemove(state, world, pos, newState, moving)
  }

  override def canConnect(world: IBlockReader, origin: BlockPos, target: BlockPos): Boolean =
    getTE(world, origin).exists(_.modules.contains(target))

  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType = {
    if (player.isCrouching) return ActionResultType.PASS
    if (world.isClientSide) {
      ActionResultType.SUCCESS
    } else {
      getTE(world, pos).onClick(player)
      ActionResultType.CONSUME
    }
  }
}


