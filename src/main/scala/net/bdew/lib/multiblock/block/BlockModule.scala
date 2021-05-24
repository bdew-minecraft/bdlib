package net.bdew.lib.multiblock.block

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.Text
import net.bdew.lib.block.{HasTE, StatefulBlock}
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.multiblock.{ModuleType, Tools}
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.{AbstractBlock, Block, BlockState}
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.{BlockItemUseContext, ItemStack}
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Hand, Util}
import net.minecraft.world.{IBlockReader, World}

abstract class BlockModule[T <: TileModule](props: AbstractBlock.Properties, val kind: ModuleType)
  extends StatefulBlock(props) with HasTE[T] with ConnectedTextureBlock {

  override def setPlacedBy(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    super.setPlacedBy(world, pos, state, placer, stack)
    getTE(world, pos).tryConnect()
  }

  override def getStateForPlacement(ctx: BlockItemUseContext): BlockState = {
    if (Tools.findConnections(ctx.getLevel, ctx.getClickedPos, kind).size <= 1)
      super.getStateForPlacement(ctx)
    else
      null
  }

  override def neighborChanged(state: BlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    getTE(world, pos).tryConnect()
  }

  override def onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moving: Boolean): Unit = {
    if (!newState.is(this))
      getTE(world, pos).onBreak()
    super.onRemove(state, world, pos, newState, moving)
  }

  override def canConnect(world: IBlockReader, origin: BlockPos, target: BlockPos): Boolean = {
    getTE(world, origin) exists { me =>
      me.connected.contains(target) || (world.getTileSafe[TileModule](target) exists { other =>
        me.getCore.isDefined && other.getCore == me.getCore
      })
    }
  }

  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType = {
    if (player.isCrouching) return ActionResultType.PASS
    if (world.isClientSide) {
      ActionResultType.SUCCESS
    } else {
      val te = getTE(world, pos)
      if (!te.getCore.exists(activateGui(state, world, pos, _, player))) {
        player.sendMessage(Text.translate("bdlib.multiblock.notconnected"), Util.NIL_UUID)
      }
      ActionResultType.CONSUME
    }
  }

  def activateGui(state: BlockState, world: World, pos: BlockPos, controller: TileController, player: PlayerEntity): Boolean = {
    controller.onClick(player)
    true
  }
}
