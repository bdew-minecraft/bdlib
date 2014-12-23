/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.gui.Color
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

case class FaceOverlay(icon: IIcon, color: Color) {
  def this(icon: IIcon, color: (Float, Float, Float)) = this(icon, new Color(color._1, color._2, color._3))
}

trait BlockAdditionalRender {
  def getFaceOverlays(world: IBlockAccess, x: Int, y: Int, z: Int, face: ForgeDirection): List[FaceOverlay]
}
