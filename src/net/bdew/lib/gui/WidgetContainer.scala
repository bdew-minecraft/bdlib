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
import net.minecraft.client.renderer.Tessellator

trait WidgetContainer {
  val rect: Rect
  val widgets = mutable.MutableList.empty[BaseWidget]

  def activeWidgets: Iterable[BaseWidget] = widgets

  def getFontRenderer: FontRenderer

  def clear() = widgets.clear()

  def add[T <: BaseWidget](w: T): T = {
    widgets += w
    w.init(this)
    return w
  }

  def mouseClicked(p: Point, bt: Int) {
    for (w <- activeWidgets)
      if (w.rect.contains(p))
        w.mouseClicked(p - w.rect.origin, bt)
      else
        w.looseFocus()
  }

  def keyTyped(c: Char, i: Int): Boolean = {
    for (w <- activeWidgets)
      if (w.keyTyped(c, i))
        return true
    return false
  }

  def draw(mouse: Point) {
    val p = mouse - rect.origin
    GL11.glPushMatrix()
    GL11.glTranslatef(rect.origin.x, rect.origin.y, 0)
    for (w <- activeWidgets) {
      w.draw(p)
    }
    GL11.glPopMatrix()
  }

  def handleTooltip(op: Point, tip: mutable.MutableList[String]) {
    val p = op
    for (w <- activeWidgets if w.rect.contains(p))
      w.handleTooltip(p - w.rect.origin, tip)
  }

  def drawTexture(r: Rect, t: TextureLocation) {
    Minecraft.getMinecraft.renderEngine.bindTexture(t.resource)
    drawTexture(r, t.p)
  }

  def looseFocus() =
    for (w <- activeWidgets)
      w.looseFocus()

  def getOffsetFromWindow: Point

  def drawTexture(r: Rect, uv: Point, color: Color = Color.white): Unit
  def drawIcon(r: Rect, i: Icon, color: Color = Color.white)
  def drawTextureScaled(r: Rect, t: TextureLocationScaled, color: Color = Color.white)
}

class WidgetContainerWindow(val parent: BaseScreen, xSz: Int, ySz: Int) extends WidgetContainer {
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = Point(0, 0)
  val rect = Rect(0, 0, xSz, ySz)

  final val F = 1/256F

  def addVertexUVScaled(x: Float, y: Float, z: Float, u: Float, v: Float) {
    Tessellator.instance.addVertexWithUV(x, y, z, u * F, v * F)
  }

  def drawTextureScaled(r: Rect, l: TextureLocationScaled, color: Color = Color.white) {
    val t = l.r
    val z = parent.getZLevel

    Minecraft.getMinecraft.renderEngine.bindTexture(l.resource)
    color.activate()
    Tessellator.instance.startDrawingQuads()
    addVertexUVScaled(r.x, r.y + r.w, z, t.x, t.y + t.w)
    addVertexUVScaled(r.x + r.h, r.y + r.w, z, t.x + t.h, t.y + t.w)
    addVertexUVScaled(r.x + r.h, r.y, z, t.x + t.h, t.y)
    addVertexUVScaled(r.x, r.y, z, t.x, t.y)
    Tessellator.instance.draw()
  }

  def drawTexture(r: Rect, uv: Point, color: Color = Color.white) {
    color.activate()
    parent.drawTexturedModalRect(r.x, r.y, uv.x, uv.y, r.w, r.h)
  }

  def drawIcon(r: Rect, i: Icon, color: Color = Color.white) {
    color.activate()
    parent.drawTexturedModelRectFromIcon(r.x, r.y, i, r.w, r.h)
  }
}



