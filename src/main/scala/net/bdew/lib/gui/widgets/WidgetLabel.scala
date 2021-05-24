package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.util.text.ITextComponent

class WidgetLabel(text: ITextComponent, x: Float, y: Float, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    parent.drawText(m, text, rect.origin, color, false)
  }
}
