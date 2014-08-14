/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.gui

import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Color, Point, Rect, Texture}

import scala.collection.mutable

class WidgetInfo(val rect: Rect, icon: Texture, text: => String, tooltip: => String) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (iconRect.contains(p) && tooltip != null) tip += tooltip
  }

  override def draw(mouse: Point) {
    parent.drawTexture(iconRect + rect.origin, icon)
    parent.drawText(text, rect.origin +(12, 1), Color.darkgray, false)
  }
}
