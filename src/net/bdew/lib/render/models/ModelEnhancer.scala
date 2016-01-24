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
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model._

import scala.collection.JavaConversions._

/**
  * Allows extending simple models with ISmartBlockModel logic
  */
abstract class ModelEnhancer {
  /**
    * Override to specify additional textures
    */
  val additionalTextureLocations = List.empty[ResourceLocation]

  /**
    * Implement logic here
    *
    * @param base     baked version of base model
    * @param state    current block state
    * @param textures contains textures from additionalTextureLocations
    * @return modified baked model (or base if nothing is changed)
    */
  def handleState(base: IFlexibleBakedModel, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite]): IFlexibleBakedModel

  /**
    * Wrap around basic model
    *
    * @param base base model
    * @return wrapped model, that will bake into a ISmartBlockModel with our logic
    */
  def wrap(base: IModel): IRetexturableModel = new SmartUnbakedWrapper(base)

  private class SmartUnbakedWrapper(base: IModel) extends IRetexturableModel {
    override def getTextures = base.getTextures ++ additionalTextureLocations
    override def getDefaultState = base.getDefaultState
    override def getDependencies = base.getDependencies

    override def retexture(textures: ImmutableMap[String, String]) =
      if (base.isInstanceOf[IRetexturableModel]) {
        new SmartUnbakedWrapper(base.asInstanceOf[IRetexturableModel].retexture(textures))
      } else this

    override def bake(state: IModelState, format: VertexFormat, bakedTextureGetter: Function[ResourceLocation, TextureAtlasSprite]) = {
      val baked = base.bake(state, format, bakedTextureGetter)
      val additionalSprites = additionalTextureLocations.map(res => res -> bakedTextureGetter(res)).toMap
      new FlexibleBakedModelProxy(baked) with ISmartBlockModel {
        override def handleBlockState(state: IBlockState): IBakedModel = handleState(baked, state, additionalSprites)
      }
    }
  }

}

object ModelEnhancer {
  /**
    * Combines 2 ModelEnhancer's
    *
    * @return combined enhancer that will apply both e1 and e2
    */
  def compose(e1: ModelEnhancer, e2: ModelEnhancer) = new ModelEnhancer {
    override val additionalTextureLocations: List[ResourceLocation] = e1.additionalTextureLocations ++ e2.additionalTextureLocations
    override def handleState(base: IFlexibleBakedModel, state: IBlockState, additionalSprites: Map[ResourceLocation, TextureAtlasSprite]) = {
      e2.handleState(e1.handleState(base, state, additionalSprites), state, additionalSprites)
    }
  }
}
