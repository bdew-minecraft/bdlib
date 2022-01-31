package net.bdew.lib.render

import com.mojang.math.Transformation
import net.bdew.lib.render.primitive._
import net.minecraft.core.Direction

/**
 * Generates quads for a cuboid of the given size
 */
object Cuboid {
  private val identity = Transformation.identity()

  def cuboidAsList(v1: Vertex, v2: Vertex, texture: Texture, trans: Transformation = identity, tint: Int = -1): List[TQuad] =
    Direction.values().toList map (f => face(v1, v2, f, texture, trans, tint))

  def cuboidAsMap(v1: Vertex, v2: Vertex, texture: Texture, trans: Transformation = identity, tint: Int = -1): Map[Direction, TQuad] =
    Direction.values().toList.map(f => trans.rotateTransform(f) -> face(v1, v2, f, texture, trans, tint)).toMap

  def face(v1: Vertex, v2: Vertex, f: Direction, texture: Texture, trans: Transformation = identity, tint: Int = -1): TQuad =
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
