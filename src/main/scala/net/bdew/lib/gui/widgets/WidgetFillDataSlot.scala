package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.data.mixins.DataSlotNumeric
import net.bdew.lib.gui._

class WidgetFillDataSlot[T](val rect: Rect, val texture: Texture, dir: Direction.Direction, dSlot: DataSlotNumeric[T], maxVal: T)(implicit num: Numeric[T]) extends Widget {

  import num.mkNumericOps

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    val fill = dSlot.value.toFloat / maxVal.toFloat
    dir match {
      case Direction.UP =>
        parent.drawTextureInterpolate(m, rect, texture, 0, 1 - fill, 1, 1)
      case Direction.DOWN =>
        parent.drawTextureInterpolate(m, rect, texture, 0, 0, 1, fill)
      case Direction.LEFT =>
        parent.drawTextureInterpolate(m, rect, texture, 1 - fill, 0, 1, 1)
      case Direction.RIGHT =>
        parent.drawTextureInterpolate(m, rect, texture, 0, 0, fill, 1)
    }
  }
}


