package net.bdew.lib.multiblock.block

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.Text
import net.bdew.lib.block.{HasTE, StatefulBlock}
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.multiblock.{ModuleType, Tools}
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

abstract class BlockModule[T <: TileModule](props: BlockBehaviour.Properties, val kind: ModuleType)
  extends StatefulBlock(props) with HasTE[T] with ConnectedTextureBlock {

  override def setPlacedBy(world: Level, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    super.setPlacedBy(world, pos, state, placer, stack)
    getTE(world, pos).tryConnect()
  }

  override def getStateForPlacement(ctx: BlockPlaceContext): BlockState = {
    if (Tools.findConnections(ctx.getLevel, ctx.getClickedPos, kind).size <= 1)
      super.getStateForPlacement(ctx)
    else
      null
  }

  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    getTE(world, pos).tryConnect()
  }

  override def onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    if (!newState.is(this))
      getTE(world, pos).onBreak()
    super.onRemove(state, world, pos, newState, moving)
  }

  override def canConnect(world: BlockGetter, origin: BlockPos, target: BlockPos): Boolean = {
    getTE(world, origin) exists { me =>
      me.connected.contains(target) || (world.getTileSafe[TileModule](target) exists { other =>
        me.getCore.isDefined && other.getCore == me.getCore
      })
    }
  }

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (player.isCrouching) return InteractionResult.PASS
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else {
      val te = getTE(world, pos)
      if (!te.getCore.exists(activateGui(state, world, pos, _, player))) {
        player.sendSystemMessage(Text.translate("bdlib.multiblock.notconnected"))
      }
      InteractionResult.CONSUME
    }
  }

  def activateGui(state: BlockState, world: Level, pos: BlockPos, controller: TileController, player: Player): Boolean = {
    controller.onClick(player)
    true
  }
}
