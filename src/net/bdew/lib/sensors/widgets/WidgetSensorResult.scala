/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.widgets

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.sensors.SensorSystem

import scala.collection.mutable

class WidgetSensorResult[T](val p: Point, state: => T, system: SensorSystem[_, T]) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) =
    tip += system.getLocalizedResultText(state)

  override def draw(mouse: Point) =
    system.drawResult(state, rect, parent)
}
