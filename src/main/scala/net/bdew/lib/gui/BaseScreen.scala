package net.bdew.lib.gui

import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.screens.inventory.{AbstractContainerScreen, MenuAccess}
import net.minecraft.client.gui.{Font, GuiGraphics}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.tooltip.TooltipComponent

import java.util.Optional
import scala.collection.mutable.ArrayBuffer

abstract class BaseScreen[T <: AbstractContainerMenu](container: T, playerInv: Inventory, title: Component)
  extends AbstractContainerScreen[T](container, playerInv, title)
    with ContainerEventHandler
    with MenuAccess[T] {

  def background: Texture
  var rect: Rect = _

  val widgets = new WidgetContainerWindow(this)

  def getFontRenderer: Font = getMinecraft.font
  def getZLevel = 0F //?

  def initGui(width: Int, height: Int): Unit = {
    imageWidth = width
    imageHeight = height
    super.init()
    rect = Rect(leftPos, topPos, width, height)
    widgets.clear()
  }

  override def mouseClicked(x: Double, y: Double, bt: Int): Boolean = {
    widgets.mouseClicked(Point(x - leftPos, y - topPos), bt) || super.mouseClicked(x, y, bt)
  }

  override def charTyped(c: Char, i: Int): Boolean =
    widgets.keyTyped(c, i) || super.charTyped(c, i)

  override def render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, pf: Float): Unit = {
    renderBackground(graphics)
    super.render(graphics, mouseX, mouseY, pf)
    val mouse = Point(mouseX, mouseY)
    widgets.draw(graphics, mouse, pf)
    val tip = ArrayBuffer.empty[Component]
    widgets.handleTooltip(mouse, tip)
    if (tip.nonEmpty) {
      import scala.jdk.CollectionConverters._
      graphics.renderTooltip(getFontRenderer, tip.asJava, Optional.empty[TooltipComponent], mouseX, mouseY)
    } else {
      renderTooltip(graphics, mouseX, mouseY)
    }
  }

  override def renderLabels(graphics: GuiGraphics, x: Int, y: Int): Unit = {
    // disable vanilla labels
  }

  override def renderBg(graphics: GuiGraphics, pf: Float, mouseX: Int, mouseY: Int): Unit = {
    widgets.drawTexture(graphics: GuiGraphics, rect, background)
    widgets.drawBackground(graphics: GuiGraphics, Point(mouseX, mouseY))
  }
}
