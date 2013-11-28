/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import scala.collection.mutable
import net.minecraft.client.gui.GuiButton
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

abstract class BaseScreen(cont: Container, xSz: Int, ySz: Int) extends GuiContainer(cont) {
  xSize = xSz
  ySize = ySz

  val widgets = mutable.MutableList.empty[Widget]
  val texture: ResourceLocation

  def rect = new Rect(guiLeft, guiTop, xSize, ySize)

  def getFontRenderer = fontRenderer

  override def initGui() {
    super.initGui()
    widgets.clear()
    buttonList.clear()
  }

  def addButton[T <: GuiButton](w: T): T = {
    buttonList.asInstanceOf[java.util.ArrayList[GuiButton]].add(w)
    return w
  }

  def addWidget[T <: Widget](w: T): T = {
    widgets += w
    w.parent = this
    return w
  }

  protected override def keyTyped(c: Char, i: Int) {
    for (w <- widgets)
      if (w.keyTyped(c, i))
        return
    super.keyTyped(c, i)
  }

  protected override def mouseClicked(x: Int, y: Int, bt: Int) {
    super.mouseClicked(x, y, bt)
    val p = new Point(x, y) - rect.origin
    for (w <- widgets if w.rect.contains(p))
      w.mouseClicked(p - w.rect.origin, bt)
  }

  protected override def drawGuiContainerForegroundLayer(x: Int, y: Int) {
    for (w <- widgets)
      w.draw()
  }

  protected override def drawScreen(x: Int, y: Int, f: Float) {
    super.drawScreen(x, y, f)

    val tip = mutable.MutableList.empty[String]

    val p = new Point(x, y) - rect.origin
    for (w <- widgets if w.rect.contains(p))
      w.handleTooltip(p - w.rect.origin, tip)

    import collection.JavaConversions._

    if (tip.size > 0)
      drawHoveringText(tip, x, y, fontRenderer)
  }

  protected def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(texture)
    val x = (width - xSize) / 2
    val y = (height - ySize) / 2
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
  }
}
