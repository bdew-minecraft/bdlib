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
import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.util.text.ITextComponent

class WidgetDynLabel(text: => ITextComponent, x: Float, y: Float, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    parent.drawText(m, text, rect.origin, color, false)
  }
}
