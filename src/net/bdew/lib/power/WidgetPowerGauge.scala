/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import net.bdew.lib.DecFormat
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect, Texture}

import scala.collection.mutable

class WidgetPowerGauge(val rect: Rect, texture: Texture, dSlot: DataSlotPower) extends Widget {
  val a = rect.map(_.round)

  override def draw(mouse: Point) {
    val fill = dSlot.stored / dSlot.capacity
    parent.drawTextureInterpolate(rect, texture, 0, 1 - fill, 1, 1)
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += DecFormat.round(dSlot.stored) + "/" + DecFormat.round(dSlot.capacity) + " MJ"
}
