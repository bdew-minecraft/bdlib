/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui._
import net.minecraft.client.gui.FontRenderer

class WidgetSubContainer(val rect: Rect) extends BaseWidget with WidgetContainer with SimpleDrawTarget {
  def getZLevel: Float = parent.getZLevel
  def getFontRenderer: FontRenderer = parent.getFontRenderer
  def getOffsetFromWindow: Point = parent.getOffsetFromWindow + rect.origin
}
