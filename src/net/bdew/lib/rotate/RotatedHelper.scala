/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rotate

import java.util

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection

object RotatedHelper {
  def getFacingFromEntity(ent: EntityLivingBase, validRotations: util.EnumSet[ForgeDirection], default: ForgeDirection): ForgeDirection = {
    val pitch = Math.round(ent.rotationPitch)
    val yaw = MathHelper.floor_double(ent.rotationYaw * 4.0F / 360.0F + 0.5D) & 3
    yaw match {
      case _ if pitch >= 50 && validRotations.contains(ForgeDirection.UP) => ForgeDirection.UP
      case _ if pitch <= -50 && validRotations.contains(ForgeDirection.DOWN) => ForgeDirection.DOWN
      case 0 if validRotations.contains(ForgeDirection.NORTH) => ForgeDirection.NORTH
      case 1 if validRotations.contains(ForgeDirection.EAST) => ForgeDirection.EAST
      case 2 if validRotations.contains(ForgeDirection.SOUTH) => ForgeDirection.SOUTH
      case 3 if validRotations.contains(ForgeDirection.WEST) => ForgeDirection.WEST
      case _ => default
    }
  }
}
