/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.helpers

import net.minecraft.util._

import scala.language.implicitConversions

class RichChatComponent(v: IChatComponent) {
  def &(that: IChatComponent) = v.appendSibling(that)

  private def applyStyle(f: ChatStyle => Unit) = {
    f(v.getChatStyle)
    v
  }

  def setColor(c: EnumChatFormatting) = applyStyle(_.setColor(c))
  def setBold(b: Boolean) = applyStyle(_.setBold(b))
  def setItalic(b: Boolean) = applyStyle(_.setItalic(b))
  def setObfuscated(b: Boolean) = applyStyle(_.setObfuscated(b))
  def setStrikethrough(b: Boolean) = applyStyle(_.setStrikethrough(b))
  def setUnderlined(b: Boolean) = applyStyle(_.setUnderlined(b))

}

object ChatHelper {

  object Color {
    val BLACK = EnumChatFormatting.BLACK
    val DARK_BLUE = EnumChatFormatting.DARK_BLUE
    val DARK_GREEN = EnumChatFormatting.DARK_GREEN
    val DARK_AQUA = EnumChatFormatting.DARK_AQUA
    val DARK_RED = EnumChatFormatting.DARK_RED
    val DARK_PURPLE = EnumChatFormatting.DARK_PURPLE
    val GOLD = EnumChatFormatting.GOLD
    val GRAY = EnumChatFormatting.GRAY
    val DARK_GRAY = EnumChatFormatting.DARK_GRAY
    val BLUE = EnumChatFormatting.BLUE
    val GREEN = EnumChatFormatting.GREEN
    val AQUA = EnumChatFormatting.AQUA
    val RED = EnumChatFormatting.RED
    val LIGHT_PURPLE = EnumChatFormatting.LIGHT_PURPLE
    val YELLOW = EnumChatFormatting.YELLOW
    val WHITE = EnumChatFormatting.WHITE
  }

  implicit def str2chat(s: String): ChatComponentText = new ChatComponentText(s)
  implicit def str2rich(s: String): RichChatComponent = new RichChatComponent(new ChatComponentText(s))
  implicit def pimpIChatComponent(v: IChatComponent): RichChatComponent = new RichChatComponent(v)

  def C(s: String) = new ChatComponentText(s)
  def L(s: String, params: IChatComponent*) = new ChatComponentTranslation(s, params: _*)
}
