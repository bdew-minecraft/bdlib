/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.power

import net.bdew.lib.gui.{Point, TextureLocation, Rect}
import scala.collection.mutable
import java.text.DecimalFormat
import net.minecraft.client.Minecraft
import net.bdew.lib.gui.widgets.Widget

class WidgetPowerGauge(val rect: Rect, texture: TextureLocation, dslot: DataSlotPower) extends Widget {
  val formater = new DecimalFormat("#,###")

  override def draw(mouse: Point) {
    Minecraft.getMinecraft.renderEngine.bindTexture(texture.resource)
    val fill = (dslot.stored / dslot.capacity * rect.h).round
    parent.drawTexture(Rect(rect.x, rect.y + rect.h - fill, rect.w, fill), Point(texture.x, texture.y + rect.h - fill))
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += formater.format(dslot.stored) + "/" + formater.format(dslot.capacity) + " MJ"
}
