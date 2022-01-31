package net.bdew.lib

import net.minecraft.ChatFormatting
import net.minecraft.network.chat._

import scala.language.implicitConversions

class RichTextComponent(val v: MutableComponent) extends AnyVal {
  def &(that: Component): MutableComponent = v.append(that)

  private def applyStyle(f: Style => Style): MutableComponent = {
    v.setStyle(f(v.getStyle))
  }

  def setColor(c: ChatFormatting): MutableComponent = applyStyle(_.withColor(c))
  def setBold(b: Boolean): MutableComponent = applyStyle(_.withBold(b))
  def setItalic(b: Boolean): MutableComponent = applyStyle(_.withItalic(b))
  def setObfuscated(b: Boolean): MutableComponent = applyStyle(_.setObfuscated(b))
  def setStrikethrough(b: Boolean): MutableComponent = applyStyle(_.setStrikethrough(b))
  def setUnderlined(b: Boolean): MutableComponent = applyStyle(_.setUnderlined(b))
}

object Text {
  def translate(key: String, args: Object*): TranslatableComponent =
    new TranslatableComponent(key, args: _*)

  def string(s: String) = new TextComponent(s)

  val empty: Component = string("")

  def unit(unit: String): TranslatableComponent = translate(s"bdlib.unit.$unit")

  def amount(n: Int, u: String): TranslatableComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Int, c: Int, u: String): TranslatableComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Int, c: Int): TranslatableComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Int, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Int): TranslatableComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Int, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Int): TranslatableComponent = amount(n, "fe")
  def energyCap(n: Int, c: Int): TranslatableComponent = withCap(n, c, "fe")
  def energyPerTick(n: Int): TranslatableComponent = perTick(n, "fe")

  def fluid(n: Int): TranslatableComponent = amount(n, "mb")
  def fluidCap(n: Int, c: Int): TranslatableComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Int): TranslatableComponent = perTick(n, "mb")

  def amount(n: Float, u: String): TranslatableComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Float, c: Float, u: String): TranslatableComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Float, c: Float): TranslatableComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Float, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Float): TranslatableComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Float, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Float): TranslatableComponent = amount(n, "fe")
  def energyCap(n: Float, c: Float): TranslatableComponent = withCap(n, c, "fe")
  def energyPerTick(n: Float): TranslatableComponent = perTick(n, "fe")

  def fluid(n: Float): TranslatableComponent = amount(n, "mb")
  def fluidCap(n: Float, c: Float): TranslatableComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Float): TranslatableComponent = perTick(n, "mb")

  def amount(n: Double, u: String): TranslatableComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Double, c: Double, u: String): TranslatableComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Double, c: Double): TranslatableComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Double, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Double): TranslatableComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Double, u: String): TranslatableComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Double): TranslatableComponent = amount(n, "fe")
  def energyCap(n: Double, c: Double): TranslatableComponent = withCap(n, c, "fe")
  def energyPerTick(n: Double): TranslatableComponent = perTick(n, "fe")

  def fluid(n: Double): TranslatableComponent = amount(n, "mb")
  def fluidCap(n: Double, c: Double): TranslatableComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Double): TranslatableComponent = perTick(n, "mb")

  implicit def pimpTextComponent(v: MutableComponent): RichTextComponent = new RichTextComponent(v)

  object Color {
    val BLACK = ChatFormatting.BLACK
    val DARK_BLUE = ChatFormatting.DARK_BLUE
    val DARK_GREEN = ChatFormatting.DARK_GREEN
    val DARK_AQUA = ChatFormatting.DARK_AQUA
    val DARK_RED = ChatFormatting.DARK_RED
    val DARK_PURPLE = ChatFormatting.DARK_PURPLE
    val GOLD = ChatFormatting.GOLD
    val GRAY = ChatFormatting.GRAY
    val DARK_GRAY = ChatFormatting.DARK_GRAY
    val BLUE = ChatFormatting.BLUE
    val GREEN = ChatFormatting.GREEN
    val AQUA = ChatFormatting.AQUA
    val RED = ChatFormatting.RED
    val LIGHT_PURPLE = ChatFormatting.LIGHT_PURPLE
    val YELLOW = ChatFormatting.YELLOW
    val WHITE = ChatFormatting.WHITE
  }
}
