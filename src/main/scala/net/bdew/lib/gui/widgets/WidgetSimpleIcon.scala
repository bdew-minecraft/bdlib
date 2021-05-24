package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui._

case class WidgetSimpleIcon(rect: Rect, icon: Texture) extends Widget {
  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = parent.drawTexture(m, rect, icon)
}
