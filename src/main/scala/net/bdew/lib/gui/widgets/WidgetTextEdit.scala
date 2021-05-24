/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.widget.TextFieldWidget

class WidgetTextEdit(val rect: Rect, fr: FontRenderer) extends
  TextFieldWidget(fr, rect.x.round, rect.y.round, rect.w.round, rect.h.round, null) with Widget {

  override def mouseClicked(p: Point, button: Int): Boolean = {
    val pp = p + rect.origin
    super.mouseClicked(pp.x.round, pp.y.round, button)
  }

  override def looseFocus(): Unit = setFocused(false)

  override def keyTyped(c: Char, i: Int): Boolean =
    charTyped(c, i)

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit =
    render(m, mouse.x.round, mouse.y.round, partial)
}
