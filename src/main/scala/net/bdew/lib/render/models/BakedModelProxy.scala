package net.bdew.lib.render.models

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.datafixers.util.Pair
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemOverrides, ItemTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.IModelData

import java.util

/**
 * Base class for wrappers around IBakedModel
 */
class BakedModelProxy(base: BakedModel) extends BakedModel {
  override def getQuads(state: BlockState, side: Direction, rand: RandomSource, extraData: IModelData): util.List[BakedQuad] =
    base.getQuads(state, side, rand, extraData)

  override def getQuads(state: BlockState, side: Direction, rand: RandomSource): util.List[BakedQuad] =
    base.getQuads(state, side, rand)

  override def useAmbientOcclusion(): Boolean = base.useAmbientOcclusion()
  override def usesBlockLight(): Boolean = base.usesBlockLight()
  override def isCustomRenderer: Boolean = base.isCustomRenderer
  override def getParticleIcon: TextureAtlasSprite = base.getParticleIcon
  override def isGui3d: Boolean = base.isGui3d
  override def getOverrides: ItemOverrides = base.getOverrides

  override def useAmbientOcclusion(state: BlockState): Boolean = base.useAmbientOcclusion(state)
  override def doesHandlePerspectives(): Boolean = base.doesHandlePerspectives()
  override def handlePerspective(cameraTransformType: ItemTransforms.TransformType, mat: PoseStack): BakedModel =
    base.handlePerspective(cameraTransformType, mat)
  override def getModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, tileData: IModelData): IModelData =
    base.getModelData(world, pos, state, tileData)
  override def getParticleIcon(data: IModelData): TextureAtlasSprite = base.getParticleIcon(data)
  override def isLayered: Boolean = base.isLayered
  override def getLayerModels(itemStack: ItemStack, fabulous: Boolean): util.List[Pair[BakedModel, RenderType]] =
    base.getLayerModels(itemStack, fabulous)
  override def getTransforms: ItemTransforms = base.getTransforms
}
