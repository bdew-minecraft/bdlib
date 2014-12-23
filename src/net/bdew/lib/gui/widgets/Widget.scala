/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui.widgets

import net.bdew.lib.gui.{Point, Rect, WidgetContainer}

import scala.collection.mutable

trait BaseWidget {
  val rect: Rect
  var parent: WidgetContainer = null
  def init(p: WidgetContainer) = parent = p
  def handleTooltip(p: Point, tip: mutable.MutableList[String])
  def mouseClicked(p: Point, button: Int)
  def keyTyped(c: Char, i: Int): Boolean
  def draw(mouse: Point)
  def drawBackground(mouse: Point)
  def looseFocus()
}

trait Widget extends BaseWidget {
  val rect: Rect
  def handleTooltip(p: Point, tip: mutable.MutableList[String]) {}
  def mouseClicked(p: Point, button: Int) {}
  def keyTyped(c: Char, i: Int): Boolean = false
  def draw(mouse: Point) {}
  def drawBackground(mouse: Point) {}
  def looseFocus() {}
}
