/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.{BakedQuad, IBakedModel}
import net.minecraft.util.EnumFacing

import scala.collection.JavaConversions._

/**
  * Simple wrapper around IBakedModel that adds additional face quads
  *
  * @param base base model
  * @param add  map of face quads to add
  */
class BakedModelAdditionalFaceQuads(base: IBakedModel, add: Map[EnumFacing, List[BakedQuad]]) extends BakedModelProxy(base) {
  override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] = super.getQuads(state, side, rand) ++ add(side)
}
