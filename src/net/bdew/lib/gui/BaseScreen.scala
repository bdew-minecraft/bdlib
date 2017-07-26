/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.Client
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import org.lwjgl.opengl.GL11

import scala.collection.mutable
import scala.util.DynamicVariable

abstract class BaseScreen(cont: Container, xSz: Int, ySz: Int) extends GuiContainer(cont) {
  xSize = xSz
  ySize = ySz

  val widgets = new WidgetContainerWindow(this, xSz, ySz)

  val background: Texture

  def rect = new Rect(guiLeft, guiTop, xSize, ySize)

  def getFontRenderer = Client.fontRenderer
  def getZLevel = zLevel

  // fixme: this is somewhat hacky, look for a better solution
  val parialFrame = new DynamicVariable(0f)

  override def initGui() {
    super.initGui()
    widgets.clear()
    buttonList.clear()
  }

  protected override def mouseClicked(x: Int, y: Int, bt: Int) {
    super.mouseClicked(x, y, bt)
    widgets.mouseClicked(Point(x, y) - rect.origin, bt)
  }

  protected override def keyTyped(c: Char, i: Int) =
    if (!widgets.keyTyped(c, i))
      super.keyTyped(c, i)

  protected override def drawGuiContainerForegroundLayer(x: Int, y: Int) = {
    GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT)
    GL11.glDisable(GL11.GL_DEPTH_TEST)
    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
    GL11.glEnable(GL11.GL_ALPHA_TEST)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glColor4f(1, 1, 1, 1)
    widgets.draw(Point(x, y) - rect.origin, parialFrame.value)
    GL11.glPopAttrib()
  }

  protected override def drawScreen(x: Int, y: Int, f: Float) {
    this.drawDefaultBackground()

    parialFrame.withValue(f) {
      super.drawScreen(x, y, f)
    }

    val tip = mutable.MutableList.empty[String]

    widgets.handleTooltip(Point(x, y) - rect.origin, tip)

    import scala.collection.JavaConversions._

    if (tip.nonEmpty)
      drawHoveringText(tip, x, y, getFontRenderer)
    else
      this.renderHoveredToolTip(x, y)
  }

  protected def drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
    if (background != null)
      widgets.drawTexture(rect, background)

    GL11.glPushMatrix()
    GL11.glTranslatef(rect.x, rect.y, 0)
    widgets.drawBackground(Point(x, y) - rect.origin)
    GL11.glPopMatrix()
  }
}
