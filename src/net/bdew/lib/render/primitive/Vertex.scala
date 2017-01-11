/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.primitive

import javax.vecmath.Vector4f

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraftforge.common.model.ITransformation

/**
  * Vertex coordinates
  */
case class Vertex(x: Float, y: Float, z: Float) {
  /**
    * Combines vertex data with texture data
    *
    * @param sprite vanilla texture sprite
    * @param uv     coordinates in sprite (0-16)
    * @return combined data, UV translated to sprite sheet coordinates
    */
  def withTexture(sprite: TextureAtlasSprite, uv: UV) =
    TVertex(x, y, z, sprite.getInterpolatedU(uv.u), sprite.getInterpolatedV(uv.v))

  /**
    * Applies a transformation to the vertex data
    */
  def applyTransformation(t: ITransformation) = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.getMatrix.transform(tmp)
    if (Math.abs(tmp.w - 1f) > 1e-5) tmp.scale(1f / tmp.w)
    Vertex(tmp.x, tmp.y, tmp.z)
  }

  def transform(transformation: (Float) => Float) = {
    Vertex(transformation(x), transformation(y), transformation(z))
  }
}

/**
  * Vertex + Texture coordinates
  */
case class TVertex(x: Float, y: Float, z: Float, u: Float, v: Float) {
  /**
    * Applies a transformation to the vertex data, leaving texture untouched
    */
  def applyTransformation(t: ITransformation) = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.getMatrix.transform(tmp)
    if (Math.abs(tmp.w - 1f) > 1e-5) tmp.scale(1f / tmp.w)
    copy(x = tmp.x, y = tmp.y, z = tmp.z)
  }
}