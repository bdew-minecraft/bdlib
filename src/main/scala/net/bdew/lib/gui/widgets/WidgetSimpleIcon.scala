package net.bdew.lib.gui.widgets

import net.bdew.lib.gui._
import net.minecraft.client.gui.GuiGraphics

case class WidgetSimpleIcon(rect: Rect, icon: Texture) extends Widget {
  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = parent.drawTexture(graphics, rect, icon)
}
