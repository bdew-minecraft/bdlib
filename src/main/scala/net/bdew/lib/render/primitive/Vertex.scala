/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.primitive

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.math.vector.{TransformationMatrix, Vector4f}

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
  def withTexture(sprite: TextureAtlasSprite, uv: UV): TVertex =
    TVertex(x, y, z, sprite.getU(uv.u), sprite.getV(uv.v))

  /**
   * Applies a transformation to the vertex data
   */
  def applyTransformation(t: TransformationMatrix): Vertex = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.transformPosition(tmp)
    if (Math.abs(tmp.w - 1f) > 1e-5) tmp.perspectiveDivide()
    Vertex(tmp.x, tmp.y, tmp.z)
  }

  def transform(transformation: Float => Float): Vertex = {
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
  def applyTransformation(t: TransformationMatrix): TVertex = {
    val tmp = new Vector4f(x, y, z, 1f)
    t.transformPosition(tmp)
    if (Math.abs(tmp.w - 1f) > 1e-5) tmp.perspectiveDivide()
    copy(x = tmp.x, y = tmp.y, z = tmp.z)
  }
}