package net.bdew.lib

import java.text.DecimalFormat

object DecFormat {
  val thousandsFmt = new DecimalFormat("#,##0")
  val dec2Fmt = new DecimalFormat("#,##0.00")
  val dec1Fmt = new DecimalFormat("#,##0.0")

  def round(x: Double): String = thousandsFmt.format(x).replace('\u00A0', ' ')
  def round(x: Float): String = thousandsFmt.format(x).replace('\u00A0', ' ')
  def round(x: Int): String = thousandsFmt.format(x).replace('\u00A0', ' ')

  def dec2(x: Double): String = dec2Fmt.format(x).replace('\u00A0', ' ')
  def dec2(x: Float): String = dec2Fmt.format(x).replace('\u00A0', ' ')
  def dec2(x: Int): String = dec2Fmt.format(x).replace('\u00A0', ' ')

  def short(x: Double): String = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
  def short(x: Float): String = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
  def short(x: Int): String = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
}
