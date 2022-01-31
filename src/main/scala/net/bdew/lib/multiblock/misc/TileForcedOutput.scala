package net.bdew.lib.multiblock.misc

import net.bdew.lib.block.WrenchableBlock
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.{TileModule, TileOutput}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

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
  override def wrenched(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack, hit: BlockHitResult): InteractionResult = {
    if (world.isClientSide) return InteractionResult.SUCCESS
    getTE(world, pos).switchSideForced(hit.getDirection)
    InteractionResult.CONSUME
  }
}