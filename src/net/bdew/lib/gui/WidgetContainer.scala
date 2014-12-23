/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.gui.widgets.BaseWidget
import net.minecraft.client.gui.FontRenderer
import org.lwjgl.opengl.GL11

import scala.collection.mutable

trait WidgetContainer extends DrawTarget {
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

  def drawBackground(mouse: Point) {
    val p = mouse - rect.origin
    GL11.glPushMatrix()
    GL11.glTranslatef(rect.origin.x, rect.origin.y, 0)
    for (w <- activeWidgets) {
      w.drawBackground(p)
    }
    GL11.glPopMatrix()
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

  def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    for (w <- activeWidgets if w.rect.contains(p))
      w.handleTooltip(p - w.rect.origin, tip)
  }

  def looseFocus() =
    for (w <- activeWidgets)
      w.looseFocus()

  def getOffsetFromWindow: Point
}

class WidgetContainerWindow(val parent: BaseScreen, xSz: Int, ySz: Int) extends WidgetContainer with SimpleDrawTarget {
  def getZLevel = parent.getZLevel
  def getFontRenderer = parent.getFontRenderer
  def getOffsetFromWindow = Point(0, 0)
  val rect = Rect(0, 0, xSz, ySz)
}
