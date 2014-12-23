/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.data.base.DataSlotNumeric
import net.bdew.lib.gui._

class WidgetFillDataSlot[T](val rect: Rect, val texture: Texture, dir: Direction.Direction, dSlot: DataSlotNumeric[T], maxVal: T)(implicit num: Numeric[T]) extends Widget {

  import num.mkNumericOps

  override def draw(mouse: Point) {
    val fill = dSlot.value.toFloat() / maxVal.toFloat()
    dir match {
      case Direction.UP =>
        parent.drawTextureInterpolate(rect, texture, 0, 1 - fill, 1, 1)
      case Direction.DOWN =>
        parent.drawTextureInterpolate(rect, texture, 0, 0, 1, fill)
      case Direction.LEFT =>
        parent.drawTextureInterpolate(rect, texture, 1 - fill, 0, 1, 1)
      case Direction.RIGHT =>
        parent.drawTextureInterpolate(rect, texture, 0, 0, fill, 1)
    }
  }
}


