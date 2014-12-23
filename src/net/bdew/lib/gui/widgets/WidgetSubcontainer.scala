/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui._

class WidgetSubContainer(val rect: Rect) extends BaseWidget with WidgetContainer with SimpleDrawTarget {
  def getZLevel = parent.getZLevel
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = parent.getOffsetFromWindow + rect.origin
}
