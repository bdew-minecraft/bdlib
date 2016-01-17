/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.model

import java.util

import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.util.EnumFacing

import scala.collection.JavaConversions._

/**
  * Simple wrapper around IBakedModel that adds additional face quads
  * @param base base model
  * @param add map of face quads to add
  */
class BakedModelAdditionalFaceQuads(base: IBakedModel, add: Map[EnumFacing, List[BakedQuad]]) extends BakedModelProxy(base) {
  override def getFaceQuads(face: EnumFacing): util.List[BakedQuad] =
    if (add.isDefinedAt(face)) super.getFaceQuads(face) ++ add(face) else super.getFaceQuads(face)
}
