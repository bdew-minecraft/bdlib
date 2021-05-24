package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.Rect

abstract class WidgetMultiPane(rect: Rect) extends WidgetSubContainer(rect) {
  def getActivePane: BaseWidget

  def addPane[T <: BaseWidget](w: T): T = {
    w.init(this)
    w
  }
  override def activeWidgets: Iterable[BaseWidget] = super.activeWidgets ++: List(getActivePane)
}
