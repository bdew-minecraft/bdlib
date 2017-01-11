/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import java.util

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.MathHelper

object RotatedHelper {
  def getFacingFromEntity(ent: EntityLivingBase, validRotations: util.EnumSet[EnumFacing], default: EnumFacing): EnumFacing = {
    val pitch = Math.round(ent.rotationPitch)
    val yaw = MathHelper.floor(ent.rotationYaw * 4.0F / 360.0F + 0.5D) & 3
    yaw match {
      case _ if pitch >= 50 && validRotations.contains(EnumFacing.UP) => EnumFacing.UP
      case _ if pitch <= -50 && validRotations.contains(EnumFacing.DOWN) => EnumFacing.DOWN
      case 0 if validRotations.contains(EnumFacing.NORTH) => EnumFacing.NORTH
      case 1 if validRotations.contains(EnumFacing.EAST) => EnumFacing.EAST
      case 2 if validRotations.contains(EnumFacing.SOUTH) => EnumFacing.SOUTH
      case 3 if validRotations.contains(EnumFacing.WEST) => EnumFacing.WEST
      case _ => default
    }
  }
}
