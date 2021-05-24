/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.render.primitive._
import net.minecraft.util.Direction
import net.minecraft.util.math.vector.TransformationMatrix

/**
 * Generates quads for a cuboid of the given size
 */
object Cuboid {
  private val identity = TransformationMatrix.identity()

  def cuboidAsList(v1: Vertex, v2: Vertex, texture: Texture, trans: TransformationMatrix = identity, tint: Int = -1): List[TQuad] =
    Direction.values().toList map (f => face(v1, v2, f, texture, trans, tint))

  def cuboidAsMap(v1: Vertex, v2: Vertex, texture: Texture, trans: TransformationMatrix = identity, tint: Int = -1): Map[Direction, TQuad] =
    Direction.values().toList.map(f => trans.rotateTransform(f) -> face(v1, v2, f, texture, trans, tint)).toMap

  def face(v1: Vertex, v2: Vertex, f: Direction, texture: Texture, trans: TransformationMatrix = identity, tint: Int = -1): TQuad =
    face(v1, v2, f).withTexture(texture, tint).applyTransformation(trans)

  def face(v1: Vertex, v2: Vertex, f: Direction): Quad = f match {
    case Direction.SOUTH =>
      Quad(List4(
        Vertex(v1.x, v2.y, v2.z),
        Vertex(v1.x, v1.y, v2.z),
        Vertex(v2.x, v1.y, v2.z),
        Vertex(v2.x, v2.y, v2.z)
      ), f)

    case Direction.NORTH =>
      Quad(List4(
        Vertex(v2.x, v2.y, v1.z),
        Vertex(v2.x, v1.y, v1.z),
        Vertex(v1.x, v1.y, v1.z),
        Vertex(v1.x, v2.y, v1.z)
      ), f)

    case Direction.EAST =>
      Quad(List4(
        Vertex(v2.x, v2.y, v2.z),
        Vertex(v2.x, v1.y, v2.z),
        Vertex(v2.x, v1.y, v1.z),
        Vertex(v2.x, v2.y, v1.z)
      ), f)

    case Direction.WEST =>
      Quad(List4(
        Vertex(v1.x, v2.y, v1.z),
        Vertex(v1.x, v1.y, v1.z),
        Vertex(v1.x, v1.y, v2.z),
        Vertex(v1.x, v2.y, v2.z)
      ), f)

    case Direction.UP =>
      Quad(List4(
        Vertex(v1.x, v2.y, v1.z),
        Vertex(v1.x, v2.y, v2.z),
        Vertex(v2.x, v2.y, v2.z),
        Vertex(v2.x, v2.y, v1.z)
      ), f)

    case Direction.DOWN =>
      Quad(List4(
        Vertex(v1.x, v1.y, v2.z),
        Vertex(v1.x, v1.y, v1.z),
        Vertex(v2.x, v1.y, v1.z),
        Vertex(v2.x, v1.y, v2.z)
      ), f)
  }
}
