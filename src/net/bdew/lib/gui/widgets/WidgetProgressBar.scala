/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.data.DataSlotFloat
import net.bdew.lib.gui._

import scala.collection.mutable

class WidgetProgressBar(rect: Rect, texture: Texture, dSlot: DataSlotFloat) extends WidgetFillDataSlot[Float](rect, texture, Direction.RIGHT, dSlot, 1) {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += "%.0f".format(dSlot.value * 100) + "%"
}
