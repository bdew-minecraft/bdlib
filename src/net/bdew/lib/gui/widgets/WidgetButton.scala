/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.GuiButton
import net.minecraft.init.SoundEvents

class WidgetButton(val rect: Rect, text: String, clicked: WidgetButton => Unit)
  extends GuiButton(0, rect.x.round, rect.y.round, rect.w.round, rect.h.round, text) with Widget {

  override def mouseClicked(p: Point, button: Int) {
    Minecraft.getMinecraft.getSoundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    clicked(this)
  }

  override def draw(mouse: Point, partial: Float) = drawButton(Minecraft.getMinecraft, mouse.x.round, mouse.y.round, partial)
}
