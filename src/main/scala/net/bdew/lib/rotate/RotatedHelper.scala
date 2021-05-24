/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import net.minecraft.entity.LivingEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.MathHelper

import java.util

object RotatedHelper {
  def getFacingFromEntity(ent: LivingEntity, validRotations: util.EnumSet[Direction], default: Direction): Direction = {
    val pitch = Math.round(ent.xRot)
    val yaw = MathHelper.floor(ent.yRot * 4.0F / 360.0F + 0.5D) & 3
    yaw match {
      case _ if pitch >= 50 && validRotations.contains(Direction.UP) => Direction.UP
      case _ if pitch <= -50 && validRotations.contains(Direction.DOWN) => Direction.DOWN
      case 0 if validRotations.contains(Direction.NORTH) => Direction.NORTH
      case 1 if validRotations.contains(Direction.EAST) => Direction.EAST
      case 2 if validRotations.contains(Direction.SOUTH) => Direction.SOUTH
      case 3 if validRotations.contains(Direction.WEST) => Direction.WEST
      case _ => default
    }
  }
}
