package net.bdew.lib.render.models

import com.mojang.datafixers.util.Pair
import net.bdew.lib.Client
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model._
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.ChunkRenderTypeSet
import net.minecraftforge.client.model.data.ModelData

import java.util
import java.util.function
import scala.jdk.CollectionConverters._

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
  def processBlockQuads(state: BlockState, side: Direction, rand: RandomSource, renderType: RenderType, data: ModelData, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = base()

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
  def processItemQuads(stack: ItemStack, side: Direction, rand: RandomSource, transform: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] = base()

  /**
   * Implement data gathering for the model here
   */
  def extendModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, base: ModelData): ModelData = base

  /**
   * override render types here
   */
  def overrideRenderTypes(state: BlockState, rand: RandomSource, data: ModelData, base: ChunkRenderTypeSet): ChunkRenderTypeSet

  /**
   * Wrap around basic model
   *
   * @param base base model
   * @return wrapped model, that will bake with our logic
   */
  def wrap(base: UnbakedModel, extraTex: Map[String, Material]): UnbakedModel = new SmartUnbakedWrapper(base, extraTex)

  def overrideAmbientOcclusion(blockState: BlockState, renderType: RenderType, base: Boolean): Boolean = base

  /**
   * Combines this with another ModelEnhancer
   *
   * @return combined enhancer
   */
  def compose(that: ModelEnhancer) = new ComposedModelEnhancer(this, that)

  private class SmartUnbakedWrapper(base: UnbakedModel, extraTex: Map[String, Material]) extends UnbakedModel {
    private val additionalMaterials = additionalTextureLocations.map(new Material(Client.blocksAtlas, _))

    override def getMaterials(modelGetter: function.Function[ResourceLocation, UnbakedModel], missingTextureErrors: util.Set[Pair[String, String]]): util.Collection[Material] =
      (base.getMaterials(modelGetter, missingTextureErrors).asScala ++ additionalMaterials.filter(_.texture().getNamespace != "_ex") ++ extraTex.values).asJavaCollection

    override def getDependencies: util.Collection[ResourceLocation] = base.getDependencies

    override def bake(bakery: ModelBakery, spriteGetter: function.Function[Material, TextureAtlasSprite], transform: ModelState, location: ResourceLocation): BakedModel = {
      val baked = base.bake(bakery, spriteGetter, transform, location)
      val additionalSprites: Map[ResourceLocation, TextureAtlasSprite] =
        additionalMaterials.map(res => res.texture() -> (
          if (res.texture().getNamespace == "_ex")
            spriteGetter(extraTex.getOrElse(res.texture().getPath, throw new RuntimeException(s"Model is missing required extra texture '${res.texture().getPath}'")))
          else
            spriteGetter(res)
          )).toMap

      new BakedModelProxy(baked) with SmartItemModel {
        override def getQuads(state: BlockState, side: Direction, rand: RandomSource, data: ModelData, renderType: RenderType): util.List[BakedQuad] =
          processBlockQuads(state, side, rand, renderType, data, additionalSprites, () => baked.getQuads(state, side, rand, data, renderType).asScala.toList).asJava

        override def getItemQuads(stack: ItemStack, side: Direction, transform: TransformType, rand: RandomSource): util.List[BakedQuad] =
          processItemQuads(stack, side, rand, transform, additionalSprites, () => super.getQuads(null, side, rand, null, null).asScala.toList).asJava

        override def getModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, tileData: ModelData): ModelData =
          extendModelData(world, pos, state, baked.getModelData(world, pos, state, tileData))

        override def useAmbientOcclusion(state: BlockState, renderType: RenderType): Boolean =
          overrideAmbientOcclusion(state, renderType, baked.useAmbientOcclusion(state, renderType))

        override def getRenderTypes(state: BlockState, rand: RandomSource, data: ModelData): ChunkRenderTypeSet =
          overrideRenderTypes(state, rand, data, baked.getRenderTypes(state, rand, data))
      }
    }
  }
}

class ComposedModelEnhancer(e1: ModelEnhancer, e2: ModelEnhancer) extends ModelEnhancer {
  override def additionalTextureLocations: List[ResourceLocation] = e1.additionalTextureLocations ++ e2.additionalTextureLocations
  override def processBlockQuads(state: BlockState, side: Direction, rand: RandomSource, renderType: RenderType, data: ModelData, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] =
    e1.processBlockQuads(state, side, rand, renderType, data, textures, () => e2.processBlockQuads(state, side, rand, renderType, data, textures, base))
  override def processItemQuads(stack: ItemStack, side: Direction, rand: RandomSource, transform: TransformType, textures: Map[ResourceLocation, TextureAtlasSprite], base: () => List[BakedQuad]): List[BakedQuad] =
    e1.processItemQuads(stack, side, rand, transform, textures, () => e2.processItemQuads(stack, side, rand, transform, textures, base))
  override def extendModelData(world: BlockAndTintGetter, pos: BlockPos, state: BlockState, base: ModelData): ModelData =
    e1.extendModelData(world, pos, state, e2.extendModelData(world, pos, state, base))
  override def overrideRenderTypes(state: BlockState, rand: RandomSource, data: ModelData, base: ChunkRenderTypeSet): ChunkRenderTypeSet =
    e1.overrideRenderTypes(state, rand, data, e2.overrideRenderTypes(state, rand, data, base))
}

