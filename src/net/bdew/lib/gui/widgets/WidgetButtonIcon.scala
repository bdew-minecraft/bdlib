/*
 * Copyright (c) bdew, 2013 - 2014
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
import net.minecraft.util.ResourceLocation

import scala.collection.mutable

class WidgetButtonIcon(p: Point, clicked: WidgetButtonIcon => Unit, baseTex: Texture, hoverTex: Texture) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p +(1, 1), 14, 14)

  var icon: Texture = null
  var hover: String = null

  override def draw(mouse: Point) {
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
    Minecraft.getMinecraft.getSoundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F))
    clicked(this)
  }
}
