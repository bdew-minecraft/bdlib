/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

import scala.language.implicitConversions

class RichBlockPos(val v: BlockPos) extends AnyVal {
  def neighbours = (EnumFacing.values() map (x => x -> v.offset(x))).toMap

  def to(that: BlockPos) =
    for {
      x <- v.getX to that.getX by (if (v.getX <= that.getX) 1 else -1)
      y <- v.getY to that.getY by (if (v.getY <= that.getY) 1 else -1)
      z <- v.getZ to that.getZ by (if (v.getZ <= that.getZ) 1 else -1)
    } yield new BlockPos(x, y, z)

  def until(that: BlockPos) =
    for {
      x <- v.getX until that.getX by (if (v.getX <= that.getX) 1 else -1)
      y <- v.getY until that.getY by (if (v.getY <= that.getY) 1 else -1)
      z <- v.getZ until that.getZ by (if (v.getZ <= that.getZ) 1 else -1)
    } yield new BlockPos(x, y, z)

  def copy(x: Int = v.getX, y: Int = v.getY, z: Int = v.getZ) = new BlockPos(x, y, z)
}

