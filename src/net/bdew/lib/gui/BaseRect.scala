/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

case class BaseRect[T: Numeric](x: T, y: T, w: T, h: T) {
  val n = implicitly[Numeric[T]]

  import n.mkNumericOps

  def this(p: BasePoint[T], w: T, h: T) = this(p.x, p.y, w, h)

  def contains(p: BasePoint[T]) = n.gteq(p.x, x) && n.gteq(p.y, y) && n.lt(p.x, x2) && n.lt(p.y, y2)

  def origin = BasePoint(x, y)

  def map[R: Numeric](f: T => R) = new BaseRect(f(x), f(y), f(w), f(h))

  def interpolate(xf1: Float, yf1: Float, xf2: Float, yf2: Float) =
    new BaseRect[Float](
      xf1 * w.toFloat() + x.toFloat(),
      yf1 * h.toFloat() + y.toFloat(),
      (xf2 - xf1) * w.toFloat(),
      (yf2 - yf1) * h.toFloat()
    )

  def +(p: BasePoint[T]) = new BaseRect(origin + p, w, h)
  def -(p: BasePoint[T]) = new BaseRect(origin - p, w, h)

  def x1 = x
  def x2 = x + w
  def y1 = y
  def y2 = y + h
}
