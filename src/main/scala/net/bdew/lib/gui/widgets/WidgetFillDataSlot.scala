package net.bdew.lib.gui.widgets

import net.bdew.lib.data.mixins.DataSlotNumeric
import net.bdew.lib.gui._
import net.minecraft.client.gui.GuiGraphics

class WidgetFillDataSlot[T](val rect: Rect, val texture: Texture, dir: Direction.Direction, dSlot: DataSlotNumeric[T], maxVal: T)(implicit num: Numeric[T]) extends Widget {

  import num.mkNumericOps

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    val fill = dSlot.value.toFloat / maxVal.toFloat
    dir match {
      case Direction.UP =>
        parent.drawTextureInterpolate(graphics, rect, texture, 0, 1 - fill, 1, 1)
      case Direction.DOWN =>
        parent.drawTextureInterpolate(graphics, rect, texture, 0, 0, 1, fill)
      case Direction.LEFT =>
        parent.drawTextureInterpolate(graphics, rect, texture, 1 - fill, 0, 1, 1)
      case Direction.RIGHT =>
        parent.drawTextureInterpolate(graphics, rect, texture, 0, 0, fill, 1)
    }
  }
}


