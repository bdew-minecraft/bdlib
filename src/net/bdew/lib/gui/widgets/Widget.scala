/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui.widgets

import scala.collection.mutable
import net.bdew.lib.gui.{Point, BaseScreen, Rect}
import net.minecraft.util.ResourceLocation
import net.minecraft.client.Minecraft

trait Widget {
  val rect: Rect
  var parent: BaseScreen = null
  def handleTooltip(p: Point, tip: mutable.MutableList[String]) {}
  def mouseClicked(p: Point, button: Int) {}
  def keyTyped(c: Char, i: Int): Boolean = false
  def draw() {}

  def bindTexture(res: ResourceLocation) = Minecraft.getMinecraft.renderEngine.bindTexture(res)
}
