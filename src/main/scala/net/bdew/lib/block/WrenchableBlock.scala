package net.bdew.lib.block

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Hand, ResourceLocation}
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

import scala.jdk.CollectionConverters._

trait WrenchableBlock extends Block {
  def wrenched(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, stack: ItemStack, hit: BlockRayTraceResult): ActionResultType

  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType = {
    val stack = player.getItemInHand(hand)
    if (!stack.isEmpty && WrenchableBlock.isWrench(stack)) {
      val res = wrenched(state, world, pos, player, hand, stack, hit)
      if (res != ActionResultType.PASS) return res
    }
    super.use(state, world, pos, player, hand, hit)
  }
}

object WrenchableBlock {
  val wrenchTags = Set(new ResourceLocation("forge:wrenches"), new ResourceLocation("forge:tools/wrench"))
  val wrenchTools = Set(ToolType.get("wrench"))
  def isWrench(stack: ItemStack): Boolean = {
    stack.getToolTypes.asScala.exists(wrenchTools.contains) ||
      stack.getItem.getTags.asScala.exists(wrenchTags.contains)
  }
}