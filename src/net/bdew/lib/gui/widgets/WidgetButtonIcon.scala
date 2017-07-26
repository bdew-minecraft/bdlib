/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Rect, Texture}
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.init.SoundEvents

import scala.collection.mutable

class WidgetButtonIcon(p: Point, clicked: WidgetButtonIcon => Unit, baseTex: Texture, hoverTex: Texture) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p + (1, 1), 14, 14)

  var icon: Texture = _
  var hover: String = _

  override def draw(mouse: Point, partial: Float) {
    if (rect.contains(mouse))
      parent.drawTexture(rect, hoverTex)
    else
      parent.drawTexture(rect, baseTex)

    if (icon != null)
      parent.drawTexture(iconRect, icon)
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (hover != null && hover > "")
      tip += hover
  }

  override def mouseClicked(p: Point, button: Int) {
    Minecraft.getMinecraft.getSoundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    clicked(this)
  }
}
