/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

case class Point(x: Int, y: Int) {
  def this(p: Point) = this(p.x, p.y)
  def +(that: Point) = new Point(this.x + that.x, this.y + that.y)
  def -(that: Point) = new Point(this.x - that.x, this.y - that.y)
}

object Point {

  import language.implicitConversions

  implicit def awt2point(p: java.awt.Point): Point = new Point(p.x, p.y)
  implicit def point2awt(p: Point): java.awt.Point = new java.awt.Point(p.x, p.y)
  implicit def tuple2point(p: (Int, Int)): Point = new Point(p._1, p._2)
}
