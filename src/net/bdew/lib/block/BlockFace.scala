/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraft.util.{BlockPos, EnumFacing}

case class BlockFace(pos: BlockPos, face: EnumFacing) {
  def x = pos.getX
  def y = pos.getY
  def z = pos.getZ
  def target = pos.add(face.getDirectionVec)
  def opposite = face.getOpposite
}

object BlockFace {
  def apply(x: Int, y: Int, z: Int, f: EnumFacing): BlockFace = BlockFace(new BlockPos(x, y, z), f)
}

