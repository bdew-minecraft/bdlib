/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.{BakedQuad, IBakedModel}
import net.minecraft.util.Direction
import net.minecraftforge.client.model.data.IModelData

import java.util.Random
import scala.jdk.CollectionConverters._

object ModelUtils {
  def getAllQuads(model: IBakedModel, bs: BlockState, data: IModelData): List[BakedQuad] = {
    Direction.values.toList.flatMap(f => model.getQuads(bs, f, new Random(), data).asScala) ++
      model.getQuads(bs, null, new Random(), data).asScala
  }
}
