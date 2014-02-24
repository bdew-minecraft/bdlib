/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import org.lwjgl.opengl.GL11

case class Color(r: Float, g: Float, b: Float, a: Float = 1) {
  def this(c: (Float, Float, Float)) = this(c._1, c._2, c._3)
  def this(c: (Float, Float, Float, Float)) = this(c._1, c._2, c._3, c._4)
  def activate() {
    GL11.glColor4f(r, g, b, a)
  }
}

object Color {
  final val white = Color(1, 1, 1)
  final val black = Color(0, 0, 0)
  final val red = Color(1, 0, 0)
  final val green = Color(0, 1, 0)
  final val blue = Color(0, 0, 1)
  final val yellow = Color(1, 1, 0)
  final val cyan = Color(0, 1, 1)
  final val magenta = Color(1, 0, 1)
}
