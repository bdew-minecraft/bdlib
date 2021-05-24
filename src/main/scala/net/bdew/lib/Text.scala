package net.bdew.lib

import net.minecraft.util.text._

import scala.language.implicitConversions

class RichTextComponent(val v: IFormattableTextComponent) extends AnyVal {
  def &(that: ITextComponent): IFormattableTextComponent = v.append(that)

  private def applyStyle(f: Style => Style): IFormattableTextComponent = {
    v.setStyle(f(v.getStyle))
  }

  def setColor(c: TextFormatting): IFormattableTextComponent = applyStyle(_.withColor(c))
  def setBold(b: Boolean): IFormattableTextComponent = applyStyle(_.withBold(b))
  def setItalic(b: Boolean): IFormattableTextComponent = applyStyle(_.withItalic(b))
  def setObfuscated(b: Boolean): IFormattableTextComponent = applyStyle(_.setObfuscated(b))
  def setStrikethrough(b: Boolean): IFormattableTextComponent = applyStyle(_.setStrikethrough(b))
  def setUnderlined(b: Boolean): IFormattableTextComponent = applyStyle(_.setUnderlined(b))
}

object Text {
  def translate(key: String, args: Object*): TranslationTextComponent =
    new TranslationTextComponent(key, args: _*)

  def string(s: String) = new StringTextComponent(s)

  val empty: ITextComponent = string("")

  def unit(unit: String): TranslationTextComponent = translate(s"bdlib.unit.$unit")

  def amount(n: Int, u: String): TranslationTextComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Int, c: Int, u: String): TranslationTextComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Int, c: Int): TranslationTextComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Int, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Int): TranslationTextComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Int, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Int): TranslationTextComponent = amount(n, "fe")
  def energyCap(n: Int, c: Int): TranslationTextComponent = withCap(n, c, "fe")
  def energyPerTick(n: Int): TranslationTextComponent = perTick(n, "fe")

  def fluid(n: Int): TranslationTextComponent = amount(n, "mb")
  def fluidCap(n: Int, c: Int): TranslationTextComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Int): TranslationTextComponent = perTick(n, "mb")

  def amount(n: Float, u: String): TranslationTextComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Float, c: Float, u: String): TranslationTextComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Float, c: Float): TranslationTextComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Float, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Float): TranslationTextComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Float, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Float): TranslationTextComponent = amount(n, "fe")
  def energyCap(n: Float, c: Float): TranslationTextComponent = withCap(n, c, "fe")
  def energyPerTick(n: Float): TranslationTextComponent = perTick(n, "fe")

  def fluid(n: Float): TranslationTextComponent = amount(n, "mb")
  def fluidCap(n: Float, c: Float): TranslationTextComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Float): TranslationTextComponent = perTick(n, "mb")

  def amount(n: Double, u: String): TranslationTextComponent = translate("bdlib.format.amount.unit", DecFormat.short(n), unit(u))
  def withCap(n: Double, c: Double, u: String): TranslationTextComponent = translate("bdlib.format.cap.unit", DecFormat.short(n), DecFormat.round(c), unit(u))
  def withCap(n: Double, c: Double): TranslationTextComponent = translate("bdlib.format.cap", DecFormat.short(n), DecFormat.round(c))
  def perTick(n: Double, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", DecFormat.short(n), unit(u))
  def perTick(n: Double): TranslationTextComponent = translate("bdlib.format.rate", DecFormat.short(n))
  def delta(n: Double, u: String): TranslationTextComponent = translate("bdlib.format.rate.unit", (if (n > 0) "+" else "") + DecFormat.short(n), unit(u))

  def energy(n: Double): TranslationTextComponent = amount(n, "fe")
  def energyCap(n: Double, c: Double): TranslationTextComponent = withCap(n, c, "fe")
  def energyPerTick(n: Double): TranslationTextComponent = perTick(n, "fe")

  def fluid(n: Double): TranslationTextComponent = amount(n, "mb")
  def fluidCap(n: Double, c: Double): TranslationTextComponent = withCap(n, c, "mb")
  def fluidPerTick(n: Double): TranslationTextComponent = perTick(n, "mb")

  implicit def pimpTextComponent(v: IFormattableTextComponent): RichTextComponent = new RichTextComponent(v)

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
}
