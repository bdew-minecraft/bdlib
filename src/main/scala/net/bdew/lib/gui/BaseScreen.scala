package net.bdew.lib.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.screens.inventory.{AbstractContainerScreen, MenuAccess}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

import scala.collection.mutable.ArrayBuffer

abstract class BaseScreen[T <: AbstractContainerMenu](container: T, playerInv: Inventory, title: Component)
  extends AbstractContainerScreen[T](container, playerInv, title)
    with ContainerEventHandler
    with MenuAccess[T] {

  val background: Texture
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

  override def render(matrix: PoseStack, mouseX: Int, mouseY: Int, pf: Float): Unit = {
    renderBackground(matrix)
    super.render(matrix, mouseX, mouseY, pf)
    val mouse = Point(mouseX, mouseY)
    widgets.draw(matrix, mouse, pf)
    val tip = ArrayBuffer.empty[Component]
    widgets.handleTooltip(mouse, tip)
    if (tip.nonEmpty) {
      import scala.jdk.CollectionConverters._
      renderComponentTooltip(matrix, tip.asJava, mouseX, mouseY)
    } else {
      renderTooltip(matrix, mouseX, mouseY)
    }
  }

  override def renderLabels(m: PoseStack, x: Int, y: Int): Unit = {
    // disable vanilla labels
  }

  override def renderBg(matrix: PoseStack, pf: Float, mouseX: Int, mouseY: Int): Unit = {
    widgets.drawTexture(matrix, rect, background)
    widgets.drawBackground(matrix, Point(mouseX, mouseY))
  }
}
