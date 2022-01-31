package net.bdew.lib.render.models

import com.mojang.datafixers.util.{Either, Pair}
import net.minecraft.client.renderer.block.model.{BlockModel, ItemOverrides, ItemTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model._
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.geometry.IModelGeometry

import java.util
import java.util.function

class EnhancedBlockModelGeometry(val enhancer: ModelEnhancer, val parent: ResourceLocation, val textures: util.Map[String, Either[Material, String]]) extends IModelGeometry[EnhancedBlockModelGeometry] {
  private var parentModel: UnbakedModel = _


  override def bake(owner: IModelConfiguration, bakery: ModelBakery, spriteGetter: function.Function[Material, TextureAtlasSprite], modelTransform: ModelState, overrides: ItemOverrides, modelLocation: ResourceLocation): BakedModel = {
    parentModel.bake(bakery, spriteGetter, modelTransform, parent)
  }

  override def getTextures(owner: IModelConfiguration, modelGetter: function.Function[ResourceLocation, UnbakedModel], missingTextureErrors: util.Set[Pair[String, String]]): util.Collection[Material] = {
    val subModel = new BlockModel(parent,
      util.Collections.emptyList(),
      textures,
      true,
      null,
      ItemTransforms.NO_TRANSFORMS,
      util.Collections.emptyList()
    )

    parentModel = enhancer.wrap(subModel)

    val parentTexures = parentModel.getMaterials(modelGetter, missingTextureErrors)

    parentTexures
  }
}
