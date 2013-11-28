/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

class Rect(val x: Int, val y: Int, val w: Int, val h: Int) {
  def this(p: Point, w: Int, h: Int) = this(p.x, p.y, w, h)
  def contains(p: Point) = p.x >= x && p.y >= y && p.x <= x + w && p.y <= y + h
  def origin = new Point(x, y)
}
