/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.primitive

import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.ITransformation

case class Quad(vertexes: List4[Vertex], face: EnumFacing) {
  /**
    * Applies a transformation function to the quad
    */
  def transform(f: (Vertex) => Vertex) = copy(vertexes = vertexes map f)

  /**
    * Applies a transformation to the vertexes, leaving other data untouched
    */
  def applyTransformation(t: ITransformation) = copy(
    vertexes map (_.applyTransformation(t))
    , face = t.rotate(face)
  )

  /**
    * Combines vertex data with texture data for the quad
    *
    * @param texture texture object
    * @param tint    tint index (if any) - used for coloring blocks via Block.colorMultiplier
    * @return
    */
  def withTexture(texture: Texture, tint: Int = -1) =
    TQuad(vertexes.combine(texture.uv) { (vertex, uv) =>
      vertex.withTexture(texture.sprite, uv)
    }, face, tint)
}

/**
  * Quad with vertexes and texture coordinates
  *
  * @param face face to which this quad belongs
  * @param tint tint index (if any) - used for coloring blocks via Block.colorMultiplier
  */
case class TQuad(vertexes: List4[TVertex], face: EnumFacing, tint: Int = -1) {
  /**
    * Applies a transformation function to the quad
    */
  def transform(f: (TVertex) => TVertex) = copy(vertexes = vertexes map f)

  /**
    * Applies a transformation to the vertexes, leaving other data untouched
    */
  def applyTransformation(t: ITransformation) = copy(
    vertexes map (_.applyTransformation(t))
    , face = t.rotate(face)
  )
}


