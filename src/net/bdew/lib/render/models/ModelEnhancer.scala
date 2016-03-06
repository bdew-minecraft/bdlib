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
import net.minecraft.item.ItemStack
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
  def additionalTextureLocations = List.empty[ResourceLocation]

  /**
    * Implement block logic here
    *
    * @param base     baked version of base model
    * @param state    current block state
    * @param textures contains textures from additionalTextureLocations
    * @return modified baked model (or base if nothing is changed)
    */
  def handleBlockState(base: IPerspectiveAwareModel, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite]): IPerspectiveAwareModel

  /**
    * Implement item logic here
    *
    * @param base     baked version of base model
    * @param stack    item stack
    * @param textures contains textures from additionalTextureLocations
    * @return modified baked model (or base if nothing is changed)
    */
  def handleItemState(base: IPerspectiveAwareModel, stack: ItemStack, textures: Map[ResourceLocation, TextureAtlasSprite]): IPerspectiveAwareModel

  trait SmartWrappedModel extends IRetexturableModel[SmartWrappedModel]

  /**
    * Wrap around basic model
    *
    * @param base base model
    * @return wrapped model, that will bake into a ISmartBlockModel with our logic
    */
  def wrap(base: IModel): IRetexturableModel[SmartWrappedModel] = new SmartUnbakedWrapper(base)

  /**
    * Combines this with another ModelEnhancer
    *
    * @return combined enhancer
    */
  def compose(that: ModelEnhancer) = new ComposedModelEnhancer(this, that)

  private class SmartUnbakedWrapper[T](base: IModel) extends SmartWrappedModel {
    override def getTextures = base.getTextures ++ additionalTextureLocations
    override def getDefaultState = base.getDefaultState
    override def getDependencies = base.getDependencies

    override def retexture(textures: ImmutableMap[String, String]) =
      if (base.isInstanceOf[IRetexturableModel[_]]) {
        new SmartUnbakedWrapper(base.asInstanceOf[IRetexturableModel[_]].retexture(textures))
      } else this

    override def bake(state: IModelState, format: VertexFormat, bakedTextureGetter: Function[ResourceLocation, TextureAtlasSprite]) = {
      val baked = ModelUtils.makePerspectiveAware(base.bake(state, format, bakedTextureGetter))
      val additionalSprites = additionalTextureLocations.map(res => res -> bakedTextureGetter(res)).toMap
      new BakedModelProxy(baked) with ISmartBlockModel with ISmartItemModel {
        override def handleBlockState(state: IBlockState): IBakedModel = ModelEnhancer.this.handleBlockState(baked, state, additionalSprites)
        override def handleItemState(stack: ItemStack) = ModelEnhancer.this.handleItemState(baked, stack, additionalSprites)
      }
    }
  }

}

class ComposedModelEnhancer(e1: ModelEnhancer, e2: ModelEnhancer) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = e1.additionalTextureLocations ++ e2.additionalTextureLocations

  override def handleBlockState(base: IPerspectiveAwareModel, state: IBlockState, textures: Map[ResourceLocation, TextureAtlasSprite]) = {
    e2.handleBlockState(e1.handleBlockState(base, state, textures), state, textures)
  }

  override def handleItemState(base: IPerspectiveAwareModel, stack: ItemStack, textures: Map[ResourceLocation, TextureAtlasSprite]) = {
    e2.handleItemState(e1.handleItemState(base, stack, textures), stack, textures)
  }
}

