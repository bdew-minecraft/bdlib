/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.Misc
import org.lwjgl.opengl.GL11

case class Color(r: Float, g: Float, b: Float, a: Float = 1) {
  def this(c: (Float, Float, Float)) = this(c._1, c._2, c._3)
  def this(c: (Float, Float, Float, Float)) = this(c._1, c._2, c._3, c._4)
  def activate() = GL11.glColor4f(r, g, b, a)
  def asARGB =
    Misc.clamp((a * 255).round, 0, 255) << 24 |
      Misc.clamp((r * 255).round, 0, 255) << 16 |
      Misc.clamp((g * 255).round, 0, 255) << 8 |
      Misc.clamp((b * 255).round, 0, 255)
  def asRGB =
    Misc.clamp((r * 255).round, 0, 255) << 16 |
      Misc.clamp((g * 255).round, 0, 255) << 8 |
      Misc.clamp((b * 255).round, 0, 255)
}

object Color {
  def fromRGB(r: Int, g: Int, b: Int, a: Int = 255) = Color(r / 255F, g / 255F, b / 255F, a / 255F)
  def fromInt(v: Int) = Color(
    ((v >> 16) & 0xFF) / 255F,
    ((v >> 8) & 0xFF) / 255F,
    (v & 0xFF) / 255F
  )
  final val white = Color(1, 1, 1)
  final val black = Color(0, 0, 0)
  final val red = Color(1, 0, 0)
  final val green = Color(0, 1, 0)
  final val blue = Color(0, 0, 1)
  final val yellow = Color(1, 1, 0)
  final val cyan = Color(0, 1, 1)
  final val magenta = Color(1, 0, 1)

  final val gray = Color(0.5F, 0.5F, 0.5F)
  final val lightGray = Color(0.75F, 0.75F, 0.75F)
  final val darkGray = Color(0.25F, 0.25F, 0.25F)
}
