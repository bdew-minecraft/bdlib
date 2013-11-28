/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{TextureLocation, Rect}
import net.bdew.lib.gui.Direction
import net.bdew.lib.data.base.DataSlotNumeric

class WidgetFillDataslot[T](val rect: Rect, val texture: TextureLocation, dir: Direction.Direction, dslot: DataSlotNumeric[T], maxval: T)(implicit num: Numeric[T]) extends Widget {

  def getFillArea(max: Int): Int = (num.toFloat(dslot) / num.toFloat(maxval) * max).round

  override def draw() {
    bindTexture(texture.resource)
    dir match {
      case Direction.UP =>
        val fill = getFillArea(rect.h)
        parent.drawTexturedModalRect(rect.x, rect.y + rect.h - fill, texture.x, texture.y + rect.h - fill, rect.w, fill)
      case Direction.DOWN =>
        val fill = getFillArea(rect.h)
        parent.drawTexturedModalRect(rect.x, rect.y, texture.x, texture.y, rect.w, fill)
      case Direction.LEFT =>
        val fill = getFillArea(rect.w)
        parent.drawTexturedModalRect(rect.x + rect.w - fill, rect.y, texture.x + rect.w - fill, texture.y, fill, rect.h)
      case Direction.RIGHT =>
        val fill = getFillArea(rect.w)
        parent.drawTexturedModalRect(rect.x, rect.y, texture.x, texture.y, fill, rect.h)
    }
  }
}


