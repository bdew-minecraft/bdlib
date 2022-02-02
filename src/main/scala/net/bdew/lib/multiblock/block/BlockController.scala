package net.bdew.lib.multiblock.block

import net.bdew.lib.block.HasTETickingServer
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

abstract class BlockController[T <: TileController](props: BlockBehaviour.Properties)
  extends Block(props) with HasTETickingServer[T] with ConnectedTextureBlock {

  override def onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    getTE(world, pos).onBreak()
    super.onRemove(state, world, pos, newState, moving)
  }

  override def canConnect(world: BlockGetter, origin: BlockPos, target: BlockPos): Boolean =
    getTE(world, origin).exists(_.modules.contains(target))

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (player.isCrouching) return InteractionResult.PASS
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else {
      getTE(world, pos).onClick(player)
      InteractionResult.CONSUME
    }
  }
}


