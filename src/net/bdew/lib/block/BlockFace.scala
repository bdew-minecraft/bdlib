/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

case class BlockFace(pos: BlockPos, face: EnumFacing) {
  def x = pos.getX
  def y = pos.getY
  def z = pos.getZ
  def target = pos.add(face.getDirectionVec)
  def opposite = face.getOpposite
  def neighbour = BlockFace.neighbourFaces(face)
}

object BlockFace {
  def apply(x: Int, y: Int, z: Int, f: EnumFacing): BlockFace = BlockFace(new BlockPos(x, y, z), f)
  lazy val neighbourFaces = EnumFacing.values.map(f => f -> new NeighbourFaces(f)).toMap

  class NeighbourFaces(d: EnumFacing) {
    val top = d match {
      case EnumFacing.UP => EnumFacing.NORTH
      case EnumFacing.DOWN => EnumFacing.SOUTH
      case _ => EnumFacing.UP
    }

    val right = d match {
      case EnumFacing.UP => EnumFacing.EAST
      case EnumFacing.DOWN => EnumFacing.EAST
      case EnumFacing.NORTH => EnumFacing.WEST
      case EnumFacing.WEST => EnumFacing.SOUTH
      case EnumFacing.SOUTH => EnumFacing.EAST
      case EnumFacing.EAST => EnumFacing.NORTH
    }

    val bottom = top.getOpposite
    val left = right.getOpposite
  }

}

