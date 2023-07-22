package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Rect, WidgetContainer}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

trait BaseWidget {
  val rect: Rect
  var parent: WidgetContainer = _
  def init(p: WidgetContainer): Unit = parent = p
  def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit
  def mouseClicked(p: Point, button: Int): Boolean
  def keyTyped(c: Char, i: Int): Boolean
  def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit
  def drawBackground(graphics: GuiGraphics, mouse: Point): Unit
  def looseFocus(): Unit
}

trait Widget extends BaseWidget {
  val rect: Rect
  def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {}
  def mouseClicked(p: Point, button: Int): Boolean = false
  def keyTyped(c: Char, i: Int): Boolean = false
  def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {}
  def drawBackground(graphics: GuiGraphics, mouse: Point): Unit = {}
  def looseFocus(): Unit = {}
}
