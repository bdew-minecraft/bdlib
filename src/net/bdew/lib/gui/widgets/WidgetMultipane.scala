/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.Rect

abstract class WidgetMultipane(rect: Rect) extends WidgetSubcontainer(rect) {
  def getActivePane: BaseWidget

  def addPane[T <: BaseWidget](w: T): T = {
    w.init(this)
    return w
  }
  override def activeWidgets = super.activeWidgets ++: List(getActivePane)
}
