/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.gui.{FontRenderer, GuiTextField}

class WidgetTextEdit(val rect: Rect, fr: FontRenderer) extends GuiTextField(fr, rect.x.round, rect.y.round, rect.w.round, rect.h.round) with Widget {

  override def mouseClicked(p: Point, button: Int) = {
    val pp = p + rect.origin
    super.mouseClicked(pp.x.round, pp.y.round, button)
  }

  override def looseFocus() = setFocused(false)
  override def keyTyped(c: Char, i: Int): Boolean = super.textboxKeyTyped(c, i)
  override def draw(mouse: Point) = super.drawTextBox()
}
