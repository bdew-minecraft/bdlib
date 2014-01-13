/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.Rect

class WidgetLabel(text: String, x: Int, y: Int, color: Int) extends Widget {
  val rect: Rect = new Rect(x, y, 0, 0)
  override def draw() {
    parent.getFontRenderer.drawString(text, x, y, color)
  }
}
