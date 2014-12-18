package net.bdew.lib

import java.text.DecimalFormat

object DecFormat {
  val thousandsFmt = new DecimalFormat("#,##0")
  val dec2Fmt = new DecimalFormat("#,##0.00")

  def round(x: Double) = thousandsFmt.format(x)
  def round(x: Float) = thousandsFmt.format(x)
  def round(x: Int) = thousandsFmt.format(x)

  def dec2(x: Double) = dec2Fmt.format(x)
  def dec2(x: Float) = dec2Fmt.format(x)
  def dec2(x: Int) = dec2Fmt.format(x)
}
