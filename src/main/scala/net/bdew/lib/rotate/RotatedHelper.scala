package net.bdew.lib.rotate

import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity

import java.util

object RotatedHelper {
  def getFacingFromEntity(ent: LivingEntity, validRotations: util.EnumSet[Direction], default: Direction): Direction = {
    val pitch = Math.round(ent.getXRot)
    val yaw = Mth.floor(ent.getYRot * 4.0F / 360.0F + 0.5D) & 3
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
