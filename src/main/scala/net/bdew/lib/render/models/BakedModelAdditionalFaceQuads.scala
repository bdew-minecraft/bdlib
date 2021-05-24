package net.bdew.lib.render.models

import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.{BakedQuad, IBakedModel}
import net.minecraft.util.Direction
import net.minecraftforge.client.model.data.IModelData

import java.util
import java.util.Random
import scala.jdk.CollectionConverters._

/**
 * Simple wrapper around IBakedModel that adds additional face quads
 *
 * @param base base model
 * @param add  map of face quads to add
 */
class BakedModelAdditionalFaceQuads(base: IBakedModel, add: Map[Direction, List[BakedQuad]]) extends BakedModelProxy(base) {
  override def getQuads(state: BlockState, side: Direction, rand: Random, extraData: IModelData): util.List[BakedQuad] =
    (super.getQuads(state, side, rand, extraData).asScala ++ add(side)).asJava
}
