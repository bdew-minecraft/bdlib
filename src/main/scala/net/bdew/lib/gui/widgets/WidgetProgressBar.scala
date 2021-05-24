package net.bdew.lib.gui.widgets

import net.bdew.lib.Text
import net.bdew.lib.data.DataSlotFloat
import net.bdew.lib.gui._
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetProgressBar(rect: Rect, texture: Texture, dSlot: DataSlotFloat) extends WidgetFillDataSlot[Float](rect, texture, Direction.RIGHT, dSlot, 1) {
  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit =
    tip += Text.string("%.0f".format(dSlot.value * 100) + "%")
}
