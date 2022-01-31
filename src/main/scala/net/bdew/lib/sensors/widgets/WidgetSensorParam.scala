package net.bdew.lib.sensors.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.sensors.SensorPair
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetSensorParam[T](val p: Point, config: => SensorPair[T, _], obj: => Option[T]) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    for (x <- obj; s <- config.sensor.getParamTooltip(x, config.param)) tip += s

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit =
    obj foreach (x => config.sensor.drawParameter(m, rect, parent, x, config.param))
}
