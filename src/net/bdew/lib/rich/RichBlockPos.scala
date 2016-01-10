/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.minecraft.util.{BlockPos, EnumFacing}

import scala.language.implicitConversions

class RichBlockPos(val v: BlockPos) extends AnyVal {
  def neighbours = (EnumFacing.values() map (x => x -> v.offset(x))).toMap

  def to(that: BlockPos) =
    for {
      x <- v.getX to that.getX
      y <- v.getY to that.getZ
      z <- v.getY to that.getZ
    } yield new BlockPos(x, y, z)

  def until(that: BlockPos) =
    for {
      x <- v.getX until that.getX
      y <- v.getY until that.getZ
      z <- v.getY until that.getZ
    } yield new BlockPos(x, y, z)
}

