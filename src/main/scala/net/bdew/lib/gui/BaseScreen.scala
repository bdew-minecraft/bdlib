/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.{FontRenderer, IHasContainer, INestedGuiEventHandler}
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

abstract class BaseScreen[T <: Container](container: T, playerInv: PlayerInventory, title: ITextComponent)
  extends ContainerScreen[T](container, playerInv, title)
    with INestedGuiEventHandler
    with IHasContainer[T] {

  val background: Texture
  var rect: Rect = _

  val widgets = new WidgetContainerWindow(this)

  def getFontRenderer: FontRenderer = getMinecraft.font
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

  override def render(matrix: MatrixStack, mouseX: Int, mouseY: Int, pf: Float): Unit = {
    renderBackground(matrix)
    super.render(matrix, mouseX, mouseY, pf)
    val mouse = Point(mouseX, mouseY)
    widgets.draw(matrix, mouse, pf)
    val tip = ArrayBuffer.empty[ITextComponent]
    widgets.handleTooltip(mouse, tip)
    if (tip.nonEmpty) {
      import scala.jdk.CollectionConverters._
      renderComponentTooltip(matrix, tip.asJava, mouseX, mouseY)
    } else {
      renderTooltip(matrix, mouseX, mouseY)
    }
  }

  override def renderLabels(m: MatrixStack, x: Int, y: Int): Unit = {
    // disable vanilla labels
  }

  override def renderBg(matrix: MatrixStack, pf: Float, mouseX: Int, mouseY: Int): Unit = {
    widgets.drawTexture(matrix, rect, background)
    widgets.drawBackground(matrix, Point(mouseX, mouseY))
  }
}
