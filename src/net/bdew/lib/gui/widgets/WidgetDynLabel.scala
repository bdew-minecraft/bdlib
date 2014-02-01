/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/deepcore
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/deepcore/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect}

class WidgetDynLabel(text: =>String, x: Int, y: Int, color: Int) extends Widget {
  val rect: Rect = new Rect(x, y, 0, 0)
  override def draw(mouse: Point) {
    parent.getFontRenderer.drawString(text, x, y, color)
  }
}
