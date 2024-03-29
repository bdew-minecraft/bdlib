package net.bdew.lib.render.models

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.ModelData

import java.util
import scala.jdk.CollectionConverters._

/**
 * Simple wrapper around IBakedModel that adds additional face quads
 *
 * @param base base model
 * @param add  map of face quads to add
 */
class BakedModelAdditionalFaceQuads(base: BakedModel, add: Map[Direction, List[BakedQuad]]) extends BakedModelProxy(base) {
  override def getQuads(state: BlockState, side: Direction, rand: RandomSource, data: ModelData, renderType: RenderType): util.List[BakedQuad] =
    (super.getQuads(state, side, rand, data, renderType).asScala ++ add(side)).asJava
}
