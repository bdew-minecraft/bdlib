package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui._

case class WidgetSimpleIcon(rect: Rect, icon: Texture) extends Widget {
  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = parent.drawTexture(m, rect, icon)
}
