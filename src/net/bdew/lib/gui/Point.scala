/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

class Point(val x: Int, val y: Int) {
  def this(p: Point) = this(p.x, p.y)
  def +(that: Point) = new Point(this.x + that.x, this.y + that.y)
  def -(that: Point) = new Point(this.x - that.x, this.y - that.y)
}
