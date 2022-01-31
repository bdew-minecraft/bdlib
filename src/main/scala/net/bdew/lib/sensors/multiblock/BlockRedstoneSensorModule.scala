package net.bdew.lib.sensors.multiblock

import net.bdew.lib.block.{HasTETickingServer, WrenchableBlock}
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.rotate.StatefulBlockFacingSignal
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraftforge.network.NetworkHooks

trait BlockRedstoneSensorModule[T <: TileRedstoneSensorModule] extends BlockModule[T] with StatefulBlockFacingSignal with WrenchableBlock with HasTETickingServer[T] {
  override def activateGui(state: BlockState, world: Level, pos: BlockPos, controller: TileController, player: Player): Boolean = {
    player match {
      case serverPlayer: ServerPlayer =>
        NetworkHooks.openGui(serverPlayer, getTE(world, pos), pos)
        true
      case _ => false
    }
  }

  def notifyTarget(w: Level, pos: BlockPos): Unit = {
    w.neighborChanged(pos.offset(getFacing(w, pos).getNormal), this, pos)
  }

  override def setSignal(world: Level, pos: BlockPos, signal: Boolean): Unit = {
    super.setSignal(world, pos, signal)
    notifyTarget(world, pos)
  }

  override def isSignalSource(state: BlockState): Boolean = true

  override def getDirectSignal(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Int =
    getSignal(state, world, pos, side)

  override def getSignal(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Int =
    if (side == getFacing(state).getOpposite && getSignal(state)) 15 else 0

  override def canConnectRedstone(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Boolean =
    side == getFacing(state).getOpposite

  override def wrenched(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack, hit: BlockHitResult): InteractionResult = {
    if (!getValidFacings.contains(hit.getDirection)) return InteractionResult.PASS
    if (world.isClientSide) return InteractionResult.SUCCESS
    setFacing(world, pos, hit.getDirection)
    InteractionResult.CONSUME
  }
}
