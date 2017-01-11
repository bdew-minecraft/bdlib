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

/**
  * Texture coordinates
  */
case class UV(u: Float, v: Float)

/**
  * Texture information
  *
  * @param sprite vanilla sprite
  * @param uv     coordinates of corners: UL, LL, LR, UR
  */
case class Texture(sprite: TextureAtlasSprite, uv: List4[UV]) {
  /**
    * Rotate n*90 degrees clockwise
    *
    * @return new Texture object with modified orientation
    */
  def rotate(n: Int) =
    copy(uv = List4((0 until 4) map (p => uv.vector((p + n) % 4))))

  /**
    * Horizontal reflection
    * UL LL LR UR => UR LR LL UL
    *
    * @return new Texture object with modified orientation
    */
  def reflectHorizontal() = copy(uv = List4(uv.v4, uv.v3, uv.v2, uv.v1))

  /**
    * Vertical reflection
    * UL LL LR UR => LL UL UR LR
    *
    * @return new Texture object with modified orientation
    */
  def reflectVertical() = copy(uv = List4(uv.v2, uv.v1, uv.v4, uv.v3))
}

object Texture {
  /**
    * Helper to make Texture of full sprite with default orientation
    */
  def apply(sprite: TextureAtlasSprite): Texture =
    Texture(sprite, UV(0, 0), UV(16, 16))

  /**
    * Helper to make Texture of partial sprite, with default orientation
    */
  def apply(sprite: TextureAtlasSprite, uv1: UV, uv2: UV): Texture =
    Texture(sprite, List4(
      UV(uv1.u, uv1.v),
      UV(uv1.u, uv2.v),
      UV(uv2.u, uv2.v),
      UV(uv2.u, uv1.v)
    ))
}