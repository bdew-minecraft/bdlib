package net.bdew.lib.multiblock.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Color, Point, Rect, Texture}
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetInfoMulti(val rect: Rect, icon: Texture, text: => Component, tooltip: => List[Component]) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    if (iconRect.contains(p - rect.origin) && tooltip != null) tip ++= tooltip
  }

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(m, iconRect + rect.origin, icon)
    parent.drawText(m, text, rect.origin + (12, 1), Color.darkGray, false)
  }
}

class WidgetInfo(rect: Rect, icon: Texture, text: => Component, tooltip: => Component) extends WidgetInfoMulti(rect, icon, text, List(tooltip))
