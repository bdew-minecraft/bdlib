/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Direction, TextureLocation, Rect}
import net.bdew.lib.data.DataSlotFloat
import scala.collection.mutable

class WidgetProgressBar(rect: Rect, texture: TextureLocation, dslot: DataSlotFloat) extends WidgetFillDataslot[Float](rect, texture, Direction.RIGHT, dslot, 1) {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += "%.0f".format(dslot.cval * 100) + "%"
}
