package net.bdew.lib.block

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

trait WrenchableBlock extends Block {
  def wrenched(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack, hit: BlockHitResult): InteractionResult

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    val stack = player.getItemInHand(hand)
    if (!stack.isEmpty && WrenchableBlock.isWrench(stack)) {
      val res = wrenched(state, world, pos, player, hand, stack, hit)
      if (res != InteractionResult.PASS) return res
    }
    super.use(state, world, pos, player, hand, hit)
  }
}

object WrenchableBlock {
  val wrenchTags = Set(
    ItemTags.create(new ResourceLocation("forge", "wrenches")),
    ItemTags.create(new ResourceLocation("forge", "tools/wrench")),
  )

  def isWrench(stack: ItemStack): Boolean = wrenchTags.exists(stack.is)
}