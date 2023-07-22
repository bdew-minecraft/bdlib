package net.bdew.lib.sensors.widgets

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.sensors.GenericSensorType
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetSensorType[T](val p: Point, sensor: => GenericSensorType[T, _], obj: => Option[T]) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    for (x <- obj; s <- sensor.getTooltip(x)) tip += s

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit =
    obj foreach (x => sensor.drawSensor(graphics, rect, parent, x))
}



