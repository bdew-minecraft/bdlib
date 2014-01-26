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

case class Color(r: Float, g: Float, b: Float) {
  def this(c: (Float, Float, Float)) = this(c._1, c._2, c._3)
  def activate() {
    GL11.glColor3f(r, g, b)
  }
}

object Color {
  final val white = Color(1, 1, 1)
}
