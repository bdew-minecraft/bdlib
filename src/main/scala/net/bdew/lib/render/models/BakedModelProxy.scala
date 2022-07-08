package net.bdew.lib.render.models

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemOverrides, ItemTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.ChunkRenderTypeSet
import net.minecraftforge.client.model.data.ModelData

import java.util

/**
 * Base class for wrappers around IBakedModel
 */
class BakedModelProxy(base: BakedModel) extends BakedModel {
  override def getQuads(state: BlockState, side: Direction, rand: RandomSource, data: ModelData, renderType: RenderType): util.List[BakedQuad] =
    base.getQuads(state, side, rand, data, renderType)

  override def getQuads(state: BlockState, side: Direction, rand: RandomSource): util.List[BakedQuad] =
    base.getQuads(state, side, rand)

  override def useAmbientOcclusion(): Boolean = base.useAmbientOcclusion()
  override def usesBlockLight(): Boolean = base.usesBlockLight()
  override def isCustomRenderer: Boolean = base.isCustomRenderer
  override def getParticleIcon: TextureAtlasSprite = base.getParticleIcon
  override def isGui3d: Boolean = base.isGui3d
  override def getOverrides: ItemOverrides = base.getOverrides

  override def applyTransform(transformType: ItemTransforms.TransformType, poseStack: PoseStack, applyLeftHandTransform: Boolean): BakedModel =
    base.applyTransform(transformType, poseStack, applyLeftHandTransform)

  override def getRenderTypes(state: BlockState, rand: RandomSource, data: ModelData): ChunkRenderTypeSet =
    base.getRenderTypes(state, rand, data)

  override def getRenderTypes(itemStack: ItemStack, fabulous: Boolean): util.List[RenderType] =
    base.getRenderTypes(itemStack, fabulous)

  override def getRenderPasses(itemStack: ItemStack, fabulous: Boolean): util.List[BakedModel] =
    base.getRenderPasses(itemStack, fabulous)

  override def useAmbientOcclusion(state: BlockState): Boolean = base.useAmbientOcclusion(state)

  override def getModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, tileData: ModelData): ModelData =
    base.getModelData(world, pos, state, tileData)

  override def getParticleIcon(data: ModelData): TextureAtlasSprite = base.getParticleIcon(data)

  override def getTransforms: ItemTransforms = base.getTransforms
}
