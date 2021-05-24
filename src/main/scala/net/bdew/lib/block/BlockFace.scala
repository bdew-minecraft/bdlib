package net.bdew.lib.block

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos

case class BlockFace(pos: BlockPos, face: Direction) {
  def x: Int = pos.getX
  def y: Int = pos.getY
  def z: Int = pos.getZ
  def target: BlockPos = pos.relative(face)
  def opposite: Direction = face.getOpposite
  def neighbour: BlockFace.NeighbourFaces = BlockFace.neighbourFaces(face)
}

object BlockFace {
  def apply(x: Int, y: Int, z: Int, f: Direction): BlockFace = BlockFace(new BlockPos(x, y, z), f)

  lazy val neighbourFaces: Map[Direction, NeighbourFaces] =
    Direction.values.map(f => f -> new NeighbourFaces(f)).toMap

  class NeighbourFaces(d: Direction) {
    val top: Direction = d match {
      case Direction.UP => Direction.NORTH
      case Direction.DOWN => Direction.SOUTH
      case _ => Direction.UP
    }

    val right: Direction = d match {
      case Direction.UP => Direction.EAST
      case Direction.DOWN => Direction.EAST
      case Direction.NORTH => Direction.WEST
      case Direction.WEST => Direction.SOUTH
      case Direction.SOUTH => Direction.EAST
      case Direction.EAST => Direction.NORTH
    }

    val bottom: Direction = top.getOpposite
    val left: Direction = right.getOpposite
  }

}

