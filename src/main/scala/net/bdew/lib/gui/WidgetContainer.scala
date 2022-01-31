package net.bdew.lib.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui.widgets.BaseWidget
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait WidgetContainer extends DrawTarget {
  def rect: Rect

  val widgets: ListBuffer[BaseWidget] = mutable.ListBuffer.empty[BaseWidget]

  def activeWidgets: Iterable[BaseWidget] = widgets

  def getFontRenderer: Font

  def clear(): Unit = widgets.clear()

  def add[T <: BaseWidget](w: T): T = {
    widgets += w
    w.init(this)
    w
  }

  def mouseClicked(mouse: Point, bt: Int): Boolean = {
    for (w <- activeWidgets)
      if (w.rect.contains(mouse)) {
        if (w.mouseClicked(mouse - w.rect.origin, bt))
          return true
      } else {
        w.looseFocus()
      }
    false
  }

  def keyTyped(c: Char, i: Int): Boolean = {
    for (w <- activeWidgets)
      if (w.keyTyped(c, i))
        return true
    false
  }

  def drawBackground(matrix: PoseStack, mouse: Point): Unit = {
    val p = mouse - rect.origin
    matrix.pushPose()
    matrix.translate(rect.origin.x, rect.origin.y, 0)
    for (w <- activeWidgets) {
      w.drawBackground(matrix, p)
    }
    matrix.popPose()
  }

  def draw(matrix: PoseStack, mouse: Point, partial: Float): Unit = {
    val p = mouse - rect.origin
    matrix.pushPose()
    matrix.translate(rect.origin.x, rect.origin.y, 0)
    for (w <- activeWidgets) {
      w.draw(matrix, p, partial)
    }
    matrix.popPose()
  }

  def handleTooltip(mouse: Point, tip: mutable.ArrayBuffer[Component]): Unit = {
    val p = mouse - rect.origin
    for (w <- activeWidgets if w.rect.contains(p)) {
      w.handleTooltip(p, tip)
    }
  }

  def looseFocus(): Unit =
    for (w <- activeWidgets)
      w.looseFocus()

  def getOffsetFromWindow: Point
}

class WidgetContainerWindow(val parent: BaseScreen[_]) extends WidgetContainer with SimpleDrawTarget {
  def getZLevel: Float = parent.getZLevel
  def getFontRenderer: Font = parent.getFontRenderer
  def getOffsetFromWindow: Point = Point(0, 0)
  def rect: Rect = parent.rect
}
