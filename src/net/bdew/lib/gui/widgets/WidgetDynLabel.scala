/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Color, Point, Rect}

class WidgetDynLabel(text: => String, x: Int, y: Int, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(mouse: Point) {
    parent.drawText(text, rect.origin, color, false)
  }
}
