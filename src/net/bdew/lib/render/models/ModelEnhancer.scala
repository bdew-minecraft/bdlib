/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util

import com.google.common.base.Function
import com.google.common.collect.ImmutableMap
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemOverrideList}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.{EnumFacing, ResourceLocation}
import net.minecraftforge.client.model._
import net.minecraftforge.common.model.IModelState

import scala.collection.JavaConversions._

/**
  * Allows extending simple models with ISmartBlockModel logic
  */
abstract class ModelEnhancer {
  /**
    * Override to specify additional textures
    */
  def additionalTextureLocations = List.empty[ResourceLocation]

  /**
    * Implement quad processing logic here
    *
    * @param state    Block state when rendering block, null for item
    * @param side     Side when rendering side, null for general quads
    * @param rand     ???
    * @param textures Map of resolved textures
    * @param base     Original list of quads
    * @return Modified list of quads
    */
  def processQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => util.List[BakedQuad]): util.List[BakedQuad]

  /**
    * Wrap around basic model
    *
    * @param base base model
    * @return wrapped model, that will bake into a ISmartBlockModel with our logic
    */
  def wrap(base: IModel): IRetexturableModel = new SmartUnbakedWrapper(base)

  val itemOverrideList = ItemOverrideList.NONE

  /**
    * Combines this with another ModelEnhancer
    *
    * @return combined enhancer
    */
  def compose(that: ModelEnhancer) = new ComposedModelEnhancer(this, that)

  private class SmartUnbakedWrapper(base: IModel) extends IRetexturableModel {
    override def getTextures = base.getTextures ++ additionalTextureLocations
    override def getDefaultState = base.getDefaultState
    override def getDependencies = base.getDependencies

    override def retexture(textures: ImmutableMap[String, String]) =
      if (base.isInstanceOf[IRetexturableModel]) {
        new SmartUnbakedWrapper(base.asInstanceOf[IRetexturableModel].retexture(textures))
      } else this

    override def bake(state: IModelState, format: VertexFormat, bakedTextureGetter: Function[ResourceLocation, TextureAtlasSprite]) = {
      val baked = ModelUtils.makePerspectiveAware(base.bake(state, format, bakedTextureGetter))
      val additionalSprites = additionalTextureLocations.map(res => res -> bakedTextureGetter(res)).toMap
      new BakedModelProxy(baked) {
        override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] =
          processQuads(state, side, rand, additionalSprites, () => super.getQuads(state, side, rand))
      }
    }
  }
}

class ComposedModelEnhancer(e1: ModelEnhancer, e2: ModelEnhancer) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = e1.additionalTextureLocations ++ e2.additionalTextureLocations
  def processQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => util.List[BakedQuad]) =
    e1.processQuads(state, side, rand, textures, () => e2.processQuads(state, side, rand, textures, base))
}

