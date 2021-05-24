/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

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
