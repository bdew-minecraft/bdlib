package net.bdew.lib.render.models

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemOverrides}
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.{ItemDisplayContext, ItemStack}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.ModelData

import java.util

/**
 * Provides a saner replacement to ISmartItemModel (RIP) via ItemOverrides
 */
trait SmartItemModel extends BakedModel {
  /**
   * Override this to provide quads for items. Normal getQuads is not called for them.
   */
  def getItemQuads(stack: ItemStack, side: Direction, ctx: ItemDisplayContext, rand: RandomSource): util.List[BakedQuad]

  final override def getOverrides: ItemOverrides = ItemOverrides

  private object ItemOverrides extends ItemOverrides() {
    override def resolve(originalModel: BakedModel, stack: ItemStack, world: ClientLevel, entity: LivingEntity, p_173469_ : Int): BakedModel = {
      new ItemModel(stack, null)
    }
  }

  private class ItemModel(stack: ItemStack, ctx: ItemDisplayContext) extends BakedModelProxy(this) {
    override def getQuads(state: BlockState, side: Direction, rand: RandomSource, data: ModelData, renderType: RenderType): util.List[BakedQuad] =
      getItemQuads(stack, side, ctx, rand)

    override def getQuads(state: BlockState, side: Direction, rand: RandomSource): util.List[BakedQuad] =
      getItemQuads(stack, side, ctx, rand)

    override def getRenderPasses(itemStack: ItemStack, fabulous: Boolean): util.List[BakedModel] =
      java.util.List.of(this)

    override def applyTransform(ctx: ItemDisplayContext, poseStack: PoseStack, applyLeftHandTransform: Boolean): BakedModel = {
      val m = new ItemModel(stack, ctx)
      m.getTransforms.getTransform(ctx).apply(applyLeftHandTransform, poseStack)
      m
    }
  }
}