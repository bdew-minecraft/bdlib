/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import scala.collection.mutable
import net.bdew.lib.gui.widgets.BaseWidget
import net.minecraft.util.Icon
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11

trait WidgetContainer {
  val rect: Rect
  val widgets = mutable.MutableList.empty[BaseWidget]

  def getFontRenderer: FontRenderer

  def clear() = widgets.clear()

  def add[T <: BaseWidget](w: T): T = {
    widgets += w
    w.init(this)
    return w
  }

  def mouseClicked(p: Point, bt: Int) {
    for (w <- widgets)
      if (w.rect.contains(p))
        w.mouseClicked(p - w.rect.origin, bt)
      else
        w.looseFocus()
  }

  def keyTyped(c: Char, i: Int): Boolean = {
    for (w <- widgets)
      if (w.keyTyped(c, i))
        return true
    return false
  }

  def draw(mouse: Point) {
    val p = mouse - rect.origin
    for (w <- widgets) {
      GL11.glPushMatrix()
      GL11.glTranslatef(rect.origin.x, rect.origin.y, 0)
      w.draw(p)
      GL11.glPopMatrix()
    }
  }

  def handleTooltip(op: Point, tip: mutable.MutableList[String]) {
    val p = op
    for (w <- widgets if w.rect.contains(p))
      w.handleTooltip(p - w.rect.origin, tip)
  }

  def drawTexture(r: Rect, t: TextureLocation) {
    Minecraft.getMinecraft.renderEngine.bindTexture(t.resource)
    drawTexture(r, t.p)
  }

  def looseFocus() =
    for (w <- widgets)
      w.looseFocus()

  def getOffsetFromWindow: Point

  def drawTexture(r: Rect, uv: Point): Unit
  def drawIcon(r: Rect, i: Icon)
}

class WidgetContainerWindow(val parent: BaseScreen, xSz: Int, ySz: Int) extends WidgetContainer {
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = Point(0, 0)
  val rect = Rect(0, 0, xSz, ySz)
  def drawTexture(r: Rect, uv: Point) =
    parent.drawTexturedModalRect(r.x, r.y, uv.x, uv.y, r.w, r.h)
  def drawIcon(r: Rect, i: Icon) =
    parent.drawTexturedModelRectFromIcon(r.x, r.y, i, r.w, r.h)
}

class WidgetSubcontainer(val rect: Rect) extends BaseWidget with WidgetContainer {
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = parent.getOffsetFromWindow + rect.origin
  def drawTexture(r: Rect, uv: Point) = parent.drawTexture(r, uv)
  def drawIcon(r: Rect, i: Icon) = parent.drawIcon(r, i)
}