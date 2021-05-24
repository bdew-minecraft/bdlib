/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.block.WrenchableBlock
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.rotate.StatefulBlockFacingSignal
import net.minecraft.block.BlockState
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Direction, Hand}
import net.minecraft.world.{IBlockReader, World}
import net.minecraftforge.fml.network.NetworkHooks

trait BlockRedstoneSensorModule[T <: TileRedstoneSensorModule] extends BlockModule[T] with StatefulBlockFacingSignal with WrenchableBlock {
  override def activateGui(state: BlockState, world: World, pos: BlockPos, controller: TileController, player: PlayerEntity): Boolean = {
    player match {
      case serverPlayer: ServerPlayerEntity =>
        NetworkHooks.openGui(serverPlayer, getTE(world, pos), pos)
        true
      case _ => false
    }
  }

  def notifyTarget(w: World, pos: BlockPos): Unit = {
    w.neighborChanged(pos.offset(getFacing(w, pos).getNormal), this, pos)
  }

  override def setSignal(world: World, pos: BlockPos, signal: Boolean): Unit = {
    super.setSignal(world, pos, signal)
    notifyTarget(world, pos)
  }

  override def isSignalSource(state: BlockState): Boolean = true

  override def getDirectSignal(state: BlockState, world: IBlockReader, pos: BlockPos, side: Direction): Int =
    getSignal(state, world, pos, side)

  override def getSignal(state: BlockState, world: IBlockReader, pos: BlockPos, side: Direction): Int =
    if (side == getFacing(state).getOpposite && getSignal(state)) 15 else 0

  override def canConnectRedstone(state: BlockState, world: IBlockReader, pos: BlockPos, side: Direction): Boolean =
    side == getFacing(state).getOpposite

  override def wrenched(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, stack: ItemStack, hit: BlockRayTraceResult): ActionResultType = {
    if (!getValidFacings.contains(hit.getDirection)) return ActionResultType.PASS
    if (world.isClientSide) return ActionResultType.SUCCESS
    setFacing(world, pos, hit.getDirection)
    ActionResultType.CONSUME
  }
}
