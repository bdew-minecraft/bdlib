/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.block.BlockRef
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

object ConnectedHelper {
  val edgeWidth = 1 / 16F

  // Represents the rectangles of all edges/corners on a 2d pane
  // 701
  // 6 2
  // 543
  // 8 is a special case for the full face
  val faceEdges = Array(
    RectF(0, 1 - edgeWidth, 1, 1),
    RectF(1 - edgeWidth, 1 - edgeWidth, 1, 1),
    RectF(1 - edgeWidth, 0, 1, 1),
    RectF(1 - edgeWidth, 0, 1, edgeWidth),
    RectF(0, 0, 1, edgeWidth),
    RectF(0, 0, edgeWidth, edgeWidth),
    RectF(0, 0, edgeWidth, 1),
    RectF(0, 1 - edgeWidth, edgeWidth, 1),
    RectF(0, 0, 1, 1)
  )

  val neighbourFaces = (for (x <- ForgeDirection.VALID_DIRECTIONS) yield x -> new neighbourFaces(x)).toMap

  // Maps edge number + face to 3d vertices
  val draw =
    (for ((rect, n) <- faceEdges.zipWithIndex; face <- ForgeDirection.VALID_DIRECTIONS)
    yield (face, n) -> new EdgeDraw(rect, face)).toMap

  val brightnessMultiplier = Map(
    ForgeDirection.UP -> 1F,
    ForgeDirection.DOWN -> 0.5F,
    ForgeDirection.NORTH -> 0.8F,
    ForgeDirection.SOUTH -> 0.8F,
    ForgeDirection.WEST -> 0.6F,
    ForgeDirection.EAST -> 0.6F
  )

  case class Vec3F(x: Float, y: Float, z: Float) {
    def *(m: Float) = Vec3F(x * m, y * m, z * m)
    def +(m: Vec3F) = Vec3F(x + m.x, y + m.y, z + m.z)
    def -(m: Vec3F) = Vec3F(x - m.x, y - m.y, z - m.z)
    def +(d: ForgeDirection) = Vec3F(x + d.offsetX, y + d.offsetY, z + d.offsetZ)
    def toInts = (x.toInt, y.toInt, z.toInt)
    def asBlockRef = BlockRef(x.toInt, y.toInt, z.toInt)
  }

  case class RectF(x1: Float, y1: Float, x2: Float, y2: Float) {
    def corners = Seq(Vec2F(x1, y1), Vec2F(x2, y1), Vec2F(x2, y2), Vec2F(x1, y2))
  }

  case class Vec2F(u: Float, v: Float)

  case class VecPair(pos: Vec3F, tex: Vec2F)

  class EdgeDraw(rect: RectF, face: ForgeDirection) {
    val vertices = rect.corners map { d =>
      val v = face match {
        case ForgeDirection.UP => Vec3F(d.u, 1, 1 - d.v)
        case ForgeDirection.DOWN => Vec3F(d.u, 0, d.v)
        case ForgeDirection.NORTH => Vec3F(1 - d.u, d.v, 0)
        case ForgeDirection.SOUTH => Vec3F(d.u, d.v, 1)
        case ForgeDirection.EAST => Vec3F(1, d.v, 1 - d.u)
        case ForgeDirection.WEST => Vec3F(0, d.v, d.u)
        case _ => sys.error("Invalid face")
      }
      VecPair(extend(v, 0.001F), d)
    }

    def doDraw(pos: Vec3F, icon: IIcon) {
      for (v <- vertices)
        addVertexWithUV(v.pos + pos, mapToIcon(v.tex, icon))
    }
  }

  def extend(v: Vec3F, o: Float) = Vec3F(
    if (v.x == 0F) -o else if (v.x == 1F) 1 + o else v.x,
    if (v.y == 0F) -o else if (v.y == 1F) 1 + o else v.y,
    if (v.z == 0F) -o else if (v.z == 1F) 1 + o else v.z
  )

  def mapToIcon(v: Vec2F, i: IIcon) = Vec2F(i.getInterpolatedU(v.u * 16), i.getInterpolatedV((1 - v.v) * 16))

  def addVertexWithUV(p: Vec3F, t: Vec2F) =
    Tessellator.instance.addVertexWithUV(p.x, p.y, p.z, t.u, t.v)

  class neighbourFaces(d: ForgeDirection) {
    val top = d match {
      case ForgeDirection.UP => ForgeDirection.NORTH
      case ForgeDirection.DOWN => ForgeDirection.SOUTH
      case _ => ForgeDirection.UP
    }

    val right = d match {
      case ForgeDirection.UP => ForgeDirection.EAST
      case ForgeDirection.DOWN => ForgeDirection.EAST
      case ForgeDirection.NORTH => ForgeDirection.WEST
      case ForgeDirection.WEST => ForgeDirection.SOUTH
      case ForgeDirection.SOUTH => ForgeDirection.EAST
      case ForgeDirection.EAST => ForgeDirection.NORTH
      case _ => ForgeDirection.UNKNOWN
    }
    val bottom = top.getOpposite
    val left = right.getOpposite
  }

}
