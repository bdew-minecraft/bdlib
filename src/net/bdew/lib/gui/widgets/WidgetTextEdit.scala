/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.minecraft.client.gui.{FontRenderer, GuiTextField}
import net.bdew.lib.gui.{Point, Rect}

class WidgetTextEdit(val rect: Rect, fr: FontRenderer) extends GuiTextField(fr, rect.x, rect.y, rect.w, rect.h) with Widget {
  override def mouseClicked(p: Point, button: Int) = super.mouseClicked(p.x + rect.x, p.y + rect.y, button)
  override def keyTyped(c: Char, i: Int): Boolean = super.textboxKeyTyped(c, i)
  override def draw() = super.drawTextBox()
}
