/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

case class BasePoint[T: Numeric](x: T, y: T) {
  val n = implicitly[Numeric[T]]

  import n.mkNumericOps

  def this(p: BasePoint[T]) = this(p.x, p.y)

  def +(that: BasePoint[T]) = BasePoint(this.x + that.x, this.y + that.y)
  def +(xOff: T, yOff: T) = BasePoint(this.x + xOff, this.y + yOff)
  def -(that: BasePoint[T]) = BasePoint(this.x - that.x, this.y - that.y)
  def -(xOff: T, yOff: T) = BasePoint(this.x - xOff, this.y - yOff)

  def map[R: Numeric](f: T => R) = new BasePoint(f(x), f(y))
}
