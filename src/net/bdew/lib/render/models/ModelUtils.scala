/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.{BakedQuad, IBakedModel}
import net.minecraft.util.EnumFacing

import scala.collection.JavaConversions._

object ModelUtils {
  def getAllQuads(model: IBakedModel, bs: IBlockState): List[BakedQuad] = {
    EnumFacing.values().toList.flatMap(f => model.getQuads(bs, f, 0)) ++ model.getQuads(bs, null, 0)
  }
}
