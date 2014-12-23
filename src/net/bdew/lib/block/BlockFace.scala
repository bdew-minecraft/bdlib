/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraftforge.common.util.ForgeDirection

case class BlockFace(x: Int, y: Int, z: Int, face: ForgeDirection) {
  def origin = BlockRef(x, y, z)
  def target = origin.neighbour(face)
  def opposite = face.getOpposite
}

object BlockFace {
  def apply(bp: BlockRef, face: ForgeDirection) = new BlockFace(bp.x, bp.y, bp.z, face)
}
