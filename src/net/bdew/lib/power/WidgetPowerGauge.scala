/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.power

import net.bdew.lib.gui.{Texture, Point, Rect}
import scala.collection.mutable
import java.text.DecimalFormat
import net.bdew.lib.gui.widgets.Widget

class WidgetPowerGauge(val rect: Rect, texture: Texture, dslot: DataSlotPower) extends Widget {
  val formater = new DecimalFormat("#,###")

  val a = rect.map(_.round)

  override def draw(mouse: Point) {
    val fill = dslot.stored / dslot.capacity
    parent.drawTextureInterpolate(rect, texture, 0, 1 - fill, 1, 1)
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += formater.format(dslot.stored) + "/" + formater.format(dslot.capacity) + " MJ"
}
