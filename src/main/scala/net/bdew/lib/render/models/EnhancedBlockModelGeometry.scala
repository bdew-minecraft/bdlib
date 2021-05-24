package net.bdew.lib.render.models

import com.mojang.datafixers.util.{Either, Pair}
import net.minecraft.client.renderer.model._
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.geometry.IModelGeometry

import java.util
import java.util.function

class EnhancedBlockModelGeometry(val enhancer: ModelEnhancer, val parent: ResourceLocation, val textures: util.Map[String, Either[RenderMaterial, String]]) extends IModelGeometry[EnhancedBlockModelGeometry] {
  private var parentModel: IUnbakedModel = _

  override def bake(owner: IModelConfiguration, bakery: ModelBakery, spriteGetter: function.Function[RenderMaterial, TextureAtlasSprite], modelTransform: IModelTransform, overrides: ItemOverrideList, modelLocation: ResourceLocation): IBakedModel = {
    parentModel.bake(bakery, spriteGetter, modelTransform, parent)
  }

  override def getTextures(owner: IModelConfiguration, modelGetter: function.Function[ResourceLocation, IUnbakedModel], missingTextureErrors: util.Set[Pair[String, String]]): util.Collection[RenderMaterial] = {
    val subModel = new BlockModel(parent,
      util.Collections.emptyList(),
      textures,
      true,
      null,
      ItemCameraTransforms.NO_TRANSFORMS,
      util.Collections.emptyList()
    )

    parentModel = enhancer.wrap(subModel)

    val parentTexures = parentModel.getMaterials(modelGetter, missingTextureErrors)

    parentTexures
  }
}
