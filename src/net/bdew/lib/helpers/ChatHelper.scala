/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.helpers

import net.minecraft.util.text._

import scala.language.implicitConversions

class RichChatComponent(v: ITextComponent) {
  def &(that: ITextComponent) = v.appendSibling(that)

  private def applyStyle(f: Style => Unit) = {
    f(v.getStyle)
    v
  }

  def setColor(c: TextFormatting) = applyStyle(_.setColor(c))
  def setBold(b: Boolean) = applyStyle(_.setBold(b))
  def setItalic(b: Boolean) = applyStyle(_.setItalic(b))
  def setObfuscated(b: Boolean) = applyStyle(_.setObfuscated(b))
  def setStrikethrough(b: Boolean) = applyStyle(_.setStrikethrough(b))
  def setUnderlined(b: Boolean) = applyStyle(_.setUnderlined(b))

}

object ChatHelper {

  object Color {
    val BLACK = TextFormatting.BLACK
    val DARK_BLUE = TextFormatting.DARK_BLUE
    val DARK_GREEN = TextFormatting.DARK_GREEN
    val DARK_AQUA = TextFormatting.DARK_AQUA
    val DARK_RED = TextFormatting.DARK_RED
    val DARK_PURPLE = TextFormatting.DARK_PURPLE
    val GOLD = TextFormatting.GOLD
    val GRAY = TextFormatting.GRAY
    val DARK_GRAY = TextFormatting.DARK_GRAY
    val BLUE = TextFormatting.BLUE
    val GREEN = TextFormatting.GREEN
    val AQUA = TextFormatting.AQUA
    val RED = TextFormatting.RED
    val LIGHT_PURPLE = TextFormatting.LIGHT_PURPLE
    val YELLOW = TextFormatting.YELLOW
    val WHITE = TextFormatting.WHITE
  }

  implicit def str2chat(s: String): TextComponentString = new TextComponentString(s)
  implicit def str2rich(s: String): RichChatComponent = new RichChatComponent(new TextComponentString(s))
  implicit def pimpITextComponent(v: ITextComponent): RichChatComponent = new RichChatComponent(v)

  def C(s: String) = new TextComponentString(s)
  def L(s: String, params: ITextComponent*) = new TextComponentTranslation(s, params: _*)
}
