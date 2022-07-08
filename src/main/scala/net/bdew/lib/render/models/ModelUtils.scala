package net.bdew.lib.render.models

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.ModelData

import scala.jdk.CollectionConverters._

object ModelUtils {
  def getAllQuads(model: BakedModel, bs: BlockState, rand: RandomSource, data: ModelData, renderType: RenderType): List[BakedQuad] = {
    Direction.values.toList.flatMap(f => model.getQuads(bs, f, rand, data, renderType).asScala) ++
      model.getQuads(bs, null, rand, data, renderType).asScala
  }
}
