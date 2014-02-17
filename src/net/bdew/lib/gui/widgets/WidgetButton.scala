/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.minecraft.client.gui.GuiButton
import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.Minecraft

class WidgetButton(val rect: Rect, text: String, clicked: WidgetButton => Unit)
  extends GuiButton(0, rect.x, rect.y, rect.w, rect.h, text) with Widget {

  override def mouseClicked(p: Point, button: Int) {
    Minecraft.getMinecraft.sndManager.playSoundFX("random.click", 1.0F, 1.0F)
    clicked(this)
  }

  override def draw(mouse: Point) = drawButton(Minecraft.getMinecraft, mouse.x, mouse.y)
}
