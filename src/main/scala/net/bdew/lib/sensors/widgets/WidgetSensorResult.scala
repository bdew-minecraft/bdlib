package net.bdew.lib.sensors.widgets

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.sensors.SensorSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetSensorResult[T](val p: Point, state: => T, system: SensorSystem[_, T]) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    tip += system.getResultText(state)

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit =
    system.drawResult(graphics, state, rect, parent)
}
