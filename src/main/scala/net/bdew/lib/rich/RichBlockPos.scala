package net.bdew.lib.rich

import net.minecraft.core.{BlockPos, Direction}

import scala.language.implicitConversions

class RichBlockPos(val v: BlockPos) extends AnyVal {
  def neighbours: Map[Direction, BlockPos] = (Direction.values() map (x => x -> (v.offset(x.getNormal): BlockPos))).toMap

  def to(that: BlockPos): IndexedSeq[BlockPos] =
    for {
      x <- v.getX to that.getX by (if (v.getX <= that.getX) 1 else -1)
      y <- v.getY to that.getY by (if (v.getY <= that.getY) 1 else -1)
      z <- v.getZ to that.getZ by (if (v.getZ <= that.getZ) 1 else -1)
    } yield new BlockPos(x, y, z)

  def until(that: BlockPos): IndexedSeq[BlockPos] =
    for {
      x <- v.getX until that.getX by (if (v.getX <= that.getX) 1 else -1)
      y <- v.getY until that.getY by (if (v.getY <= that.getY) 1 else -1)
      z <- v.getZ until that.getZ by (if (v.getZ <= that.getZ) 1 else -1)
    } yield new BlockPos(x, y, z)

  def copy(x: Int = v.getX, y: Int = v.getY, z: Int = v.getZ) = new BlockPos(x, y, z)
}

