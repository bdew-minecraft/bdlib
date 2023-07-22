package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class WidgetDynLabel(text: => Component, x: Float, y: Float, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    parent.drawText(graphics, text, rect.origin, color, false)
  }
}
