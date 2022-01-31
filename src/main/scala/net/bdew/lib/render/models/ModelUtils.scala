package net.bdew.lib.render.models

import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.data.IModelData

import java.util.Random
import scala.jdk.CollectionConverters._

object ModelUtils {
  def getAllQuads(model: BakedModel, bs: BlockState, data: IModelData): List[BakedQuad] = {
    Direction.values.toList.flatMap(f => model.getQuads(bs, f, new Random(), data).asScala) ++
      model.getQuads(bs, null, new Random(), data).asScala
  }
}
