/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.datafixers.util.Pair
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.{BakedQuad, IBakedModel, ItemCameraTransforms, ItemOverrideList}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockDisplayReader
import net.minecraftforge.client.model.data.IModelData

import java.util
import java.util.Random

/**
 * Base class for wrappers around IBakedModel
 */
class BakedModelProxy(base: IBakedModel) extends IBakedModel {
  override def getQuads(state: BlockState, side: Direction, rand: Random, extraData: IModelData): util.List[BakedQuad] =
    base.getQuads(state, side, rand, extraData)

  override def getQuads(state: BlockState, side: Direction, rand: Random): util.List[BakedQuad] =
    base.getQuads(state, side, rand)

  override def useAmbientOcclusion(): Boolean = base.useAmbientOcclusion()
  override def usesBlockLight(): Boolean = base.usesBlockLight()
  override def isCustomRenderer: Boolean = base.isCustomRenderer
  override def getParticleIcon: TextureAtlasSprite = base.getParticleIcon
  override def isGui3d: Boolean = base.isGui3d
  override def getOverrides: ItemOverrideList = base.getOverrides

  override def getBakedModel: IBakedModel = base.getBakedModel
  override def isAmbientOcclusion(state: BlockState): Boolean = base.isAmbientOcclusion(state)
  override def doesHandlePerspectives(): Boolean = base.doesHandlePerspectives()
  override def handlePerspective(cameraTransformType: ItemCameraTransforms.TransformType, mat: MatrixStack): IBakedModel =
    base.handlePerspective(cameraTransformType, mat)
  override def getModelData(world: IBlockDisplayReader, pos: BlockPos, state: BlockState, tileData: IModelData): IModelData =
    base.getModelData(world, pos, state, tileData)
  override def getParticleTexture(data: IModelData): TextureAtlasSprite =
    base.getParticleTexture(data)
  override def isLayered: Boolean = base.isLayered
  override def getLayerModels(itemStack: ItemStack, fabulous: Boolean): util.List[Pair[IBakedModel, RenderType]] =
    base.getLayerModels(itemStack, fabulous)
  override def getTransforms: ItemCameraTransforms = base.getTransforms
}
