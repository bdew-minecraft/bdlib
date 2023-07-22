package net.bdew.lib.power

import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetPowerGauge(val rect: Rect, texture: Texture, dSlot: DataSlotPower) extends Widget {
  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    val fill = dSlot.stored / dSlot.capacity
    parent.drawTextureInterpolate(graphics, rect, texture, 0, 1 - fill, 1, 1)
  }

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    tip += Text.energyCap(dSlot.stored, dSlot.capacity)
}
