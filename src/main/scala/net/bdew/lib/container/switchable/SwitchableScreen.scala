package net.bdew.lib.container.switchable

import net.bdew.lib.gui.widgets.{BaseWidget, WidgetMultiPane, WidgetSubContainer}
import net.bdew.lib.gui.{BaseScreen, Rect, Texture}

class Switcher(screen: SwitchableScreen[_ <: SwitchableContainer]) extends WidgetMultiPane(Rect(0, 0, screen.rect.w, screen.rect.h)) {
  override def getActivePane: BaseWidget = screen.modeMap(screen.getMenu.getActiveMode)
}

trait SwitchableScreen[T <: SwitchableContainer] extends BaseScreen[T] {
  var switcher: Switcher = _
  var modeMap = Map.empty[ContainerMode, BaseWidget]
  var backgrounds = Map.empty[ContainerMode, Texture]

  override def background: Texture = backgrounds(getMenu.getActiveMode)

  def addMode(m: ContainerMode, texture: Texture)(f: WidgetSubContainer => Unit): Unit = {
    val container = new WidgetSubContainer(Rect(0, 0, rect.w, rect.h))
    f(container)
    modeMap += m -> switcher.addPane(container)
    backgrounds += m -> texture
  }

  def initSwitchable(): Unit = {
    switcher = new Switcher(this)
    widgets.add(switcher)
  }

  override def onClose(): Unit = {
    if (getMenu.getActiveMode != SwitchableContainer.NormalMode)
      getMenu.activateModeClient(SwitchableContainer.NormalMode)
    else
      super.onClose()
  }
}
