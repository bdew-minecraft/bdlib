/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.model

import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.IFlexibleBakedModel

/**
  * Base class for wrappers around IBakedModel
  * @param base base model
  */
class BakedModelProxy(val base: IBakedModel) extends IBakedModel {
  override def isBuiltInRenderer = base.isBuiltInRenderer
  override def isAmbientOcclusion = base.isAmbientOcclusion
  override def isGui3d = base.isGui3d

  override def getParticleTexture = base.getParticleTexture

  override def getGeneralQuads = base.getGeneralQuads

  override def getFaceQuads(face: EnumFacing) = base.getFaceQuads(face)

  //noinspection ScalaDeprecation
  override def getItemCameraTransforms: ItemCameraTransforms = base.getItemCameraTransforms
}

/**
  * Base class for wrappers around IFlexibleBakedModel
  * @param base base model
  */
class FlexibleBakedModelProxy(override val base: IFlexibleBakedModel) extends BakedModelProxy(base) with IFlexibleBakedModel {
  override def getFormat = base.getFormat
}

