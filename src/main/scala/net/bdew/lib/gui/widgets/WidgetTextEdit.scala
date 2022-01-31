package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui.{Point, Rect}
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox

class WidgetTextEdit(val rect: Rect, fr: Font) extends
  EditBox(fr, rect.x.round, rect.y.round, rect.w.round, rect.h.round, null) with Widget {

  override def mouseClicked(p: Point, button: Int): Boolean = {
    val pp = p + rect.origin
    super.mouseClicked(pp.x.round, pp.y.round, button)
  }

  override def looseFocus(): Unit = setFocused(false)

  override def keyTyped(c: Char, i: Int): Boolean =
    charTyped(c, i)

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit =
    render(m, mouse.x.round, mouse.y.round, partial)
}
