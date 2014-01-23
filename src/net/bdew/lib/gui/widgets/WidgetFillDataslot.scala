/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, TextureLocation, Rect, Direction}
import net.bdew.lib.data.base.DataSlotNumeric

class WidgetFillDataslot[T](val rect: Rect, val texture: TextureLocation, dir: Direction.Direction, dslot: DataSlotNumeric[T], maxval: T)(implicit num: Numeric[T]) extends Widget {

  def getFillArea(max: Int): Int = (num.toFloat(dslot) / num.toFloat(maxval) * max).round

  override def draw(mouse: Point) {
    bindTexture(texture.resource)
    dir match {
      case Direction.UP =>
        val fill = getFillArea(rect.h)
        parent.drawTexture(Rect(rect.x, rect.y + rect.h - fill, rect.w, fill), Point(texture.x, texture.y + rect.h - fill))
      case Direction.DOWN =>
        val fill = getFillArea(rect.h)
        parent.drawTexture(Rect(rect.x, rect.y, rect.w, fill), Point(texture.x, texture.y))
      case Direction.LEFT =>
        val fill = getFillArea(rect.w)
        parent.drawTexture(Rect(rect.x + rect.w - fill, rect.y, fill, rect.h), Point(texture.x + rect.w - fill, texture.y))
      case Direction.RIGHT =>
        val fill = getFillArea(rect.w)
        parent.drawTexture(Rect(rect.x, rect.y, fill, rect.h), Point(texture.x, texture.y))
    }
  }
}


