package net.bdew.lib.gui.widgets

import net.bdew.lib.gui._
import net.minecraft.client.gui.FontRenderer

class WidgetSubContainer(val rect: Rect) extends BaseWidget with WidgetContainer with SimpleDrawTarget {
  def getZLevel: Float = parent.getZLevel
  def getFontRenderer: FontRenderer = parent.getFontRenderer
  def getOffsetFromWindow: Point = parent.getOffsetFromWindow + rect.origin
}
