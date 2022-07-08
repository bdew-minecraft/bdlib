package net.bdew.lib.render.models

import com.mojang.datafixers.util.{Either, Pair}
import net.minecraft.client.renderer.block.model.{BlockModel, ItemOverrides, ItemTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model._
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.geometry.{IGeometryBakingContext, IUnbakedGeometry}

import java.util
import java.util.function

class EnhancedBlockModelGeometry(val enhancer: ModelEnhancer, val parent: ResourceLocation, val textures: util.Map[String, Either[Material, String]], val extraTex: Map[String, Material]) extends IUnbakedGeometry[EnhancedBlockModelGeometry] {
  private var parentModel: UnbakedModel = _

  override def bake(ctx: IGeometryBakingContext, bakery: ModelBakery, spriteGetter: function.Function[Material, TextureAtlasSprite], modelTransform: ModelState, overrides: ItemOverrides, modelLocation: ResourceLocation): BakedModel = {
    parentModel.bake(bakery, spriteGetter, modelTransform, parent)
  }

  override def getMaterials(ctx: IGeometryBakingContext, modelGetter: function.Function[ResourceLocation, UnbakedModel], missingTextureErrors: util.Set[Pair[String, String]]): util.Collection[Material] = {
    val subModel = new BlockModel(parent,
      util.Collections.emptyList(),
      textures,
      true,
      null,
      ItemTransforms.NO_TRANSFORMS,
      util.Collections.emptyList()
    )

    parentModel = enhancer.wrap(subModel, extraTex)

    parentModel.getMaterials(modelGetter, missingTextureErrors)
  }
}
