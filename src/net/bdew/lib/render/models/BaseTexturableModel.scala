/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import com.google.common.base.Function
import com.google.common.collect.ImmutableMap
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.{IFlexibleBakedModel, IModelState, IRetexturableModel}

import scala.collection.JavaConversions._

/**
  * Base class for custom models that can accept textures from forge block states definition
  */
abstract class BaseTexturableModel extends IRetexturableModel[BaseTexturableModel] {
  val textures = Map.empty[String, String]
  override def getTextures = textures.values.map(x => new ResourceLocation(x))
  override def retexture(textures: ImmutableMap[String, String]) = new RetexturedModel(this, textures.toMap)

  override def bake(state: IModelState, format: VertexFormat, bakedTextureGetter: Function[ResourceLocation, TextureAtlasSprite]) =
    realBake(state, format, x => bakedTextureGetter(new ResourceLocation(textures(x))))

  def realBake(state: IModelState, format: VertexFormat, textureGetter: (String) => TextureAtlasSprite): IFlexibleBakedModel
}

class RetexturedModel(base: BaseTexturableModel, newTex: Map[String, String]) extends BaseTexturableModel {
  override val textures = newTex.withDefault(base.textures)
  override def getDefaultState = base.getDefaultState
  override def getDependencies = base.getDependencies
  override def realBake(state: IModelState, format: VertexFormat, textureGetter: (String) => TextureAtlasSprite) =
    base.realBake(state, format, textureGetter)
}
