package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.network.chat.Component

class WidgetLabel(text: Component, x: Float, y: Float, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    parent.drawText(m, text, rect.origin, color, false)
  }
}
