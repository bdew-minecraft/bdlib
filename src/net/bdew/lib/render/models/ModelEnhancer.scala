/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util
import java.util.function

import com.google.common.collect.ImmutableMap
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.item.ItemStack
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
    * Implement quad processing logic for blocks here
    *
    * @param state    Block state
    * @param side     Side when rendering side, null for general quads
    * @param rand     Random seed for variable models
    * @param textures Map of resolved textures
    * @param base     Original list of quads
    * @return Modified list of quads
    */
  def processBlockQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = base()

  /**
    * Implement quad processing logic for items here
    *
    * @param stack    Item stack
    * @param side     Side when rendering side, null for general quads
    * @param rand     Random seed for variable models
    * @param textures Map of resolved textures
    * @param base     Original list of quads
    * @return Modified list of quads
    */
  def processItemQuads(stack: ItemStack, side: EnumFacing, rand: Long, mode: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = base()

  /**
    * Wrap around basic model
    *
    * @param base base model
    * @return wrapped model, that will bake into a ISmartBlockModel with our logic
    */
  def wrap(base: IModel): IModel = new SmartUnbakedWrapper(base)

  /**
    * Combines this with another ModelEnhancer
    *
    * @return combined enhancer
    */
  def compose(that: ModelEnhancer) = new ComposedModelEnhancer(this, that)

  private class SmartUnbakedWrapper(base: IModel) extends IModel {
    override def getTextures = base.getTextures ++ additionalTextureLocations
    override def getDefaultState = base.getDefaultState
    override def getDependencies = base.getDependencies

    override def retexture(textures: ImmutableMap[String, String]) = wrap(base.retexture(textures))

    override def bake(state: IModelState, format: VertexFormat, bakedTextureGetter: function.Function[ResourceLocation, TextureAtlasSprite]) = {
      val baked = base.bake(state, format, bakedTextureGetter)
      val additionalSprites = additionalTextureLocations.map(res => res -> bakedTextureGetter(res)).toMap
      new BakedModelProxy(baked) with SmartItemModel {
        override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] =
          processBlockQuads(state, side, rand, additionalSprites, () => super.getQuads(state, side, rand).toList)
        override def getItemQuads(stack: ItemStack, side: EnumFacing, mode: TransformType, rand: Long): util.List[BakedQuad] =
          processItemQuads(stack, side, rand, mode, additionalSprites, () => super.getQuads(null, side, rand).toList)
      }
    }
  }

}

class ComposedModelEnhancer(e1: ModelEnhancer, e2: ModelEnhancer) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = e1.additionalTextureLocations ++ e2.additionalTextureLocations
  override def processBlockQuads(state: IBlockState, side: EnumFacing, rand: Long, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]) =
    e1.processBlockQuads(state, side, rand, textures, () => e2.processBlockQuads(state, side, rand, textures, base))
  override def processItemQuads(stack: ItemStack, side: EnumFacing, rand: Long, mode: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]) =
    e1.processItemQuads(stack, side, rand, mode, textures, () => e2.processItemQuads(stack, side, rand, mode, textures, base))
}

