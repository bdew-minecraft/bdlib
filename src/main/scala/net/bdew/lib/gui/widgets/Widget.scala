package net.bdew.lib.gui.widgets

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui.{Point, Rect, WidgetContainer}
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

trait BaseWidget {
  val rect: Rect
  var parent: WidgetContainer = _
  def init(p: WidgetContainer): Unit = parent = p
  def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit
  def mouseClicked(p: Point, button: Int): Boolean
  def keyTyped(c: Char, i: Int): Boolean
  def draw(m: MatrixStack, mouse: Point, partial: Float): Unit
  def drawBackground(m: MatrixStack, mouse: Point): Unit
  def looseFocus(): Unit
}

trait Widget extends BaseWidget {
  val rect: Rect
  def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit = {}
  def mouseClicked(p: Point, button: Int): Boolean = false
  def keyTyped(c: Char, i: Int): Boolean = false
  def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {}
  def drawBackground(m: MatrixStack, mouse: Point): Unit = {}
  def looseFocus(): Unit = {}
}
