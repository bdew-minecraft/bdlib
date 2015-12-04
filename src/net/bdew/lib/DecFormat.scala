/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import java.text.DecimalFormat

object DecFormat {
  val thousandsFmt = new DecimalFormat("#,##0")
  val dec2Fmt = new DecimalFormat("#,##0.00")
  val dec1Fmt = new DecimalFormat("#,##0.0")

  def round(x: Double) = thousandsFmt.format(x).replace('\u00A0', ' ')
  def round(x: Float) = thousandsFmt.format(x).replace('\u00A0', ' ')
  def round(x: Int) = thousandsFmt.format(x).replace('\u00A0', ' ')

  def dec2(x: Double) = dec2Fmt.format(x).replace('\u00A0', ' ')
  def dec2(x: Float) = dec2Fmt.format(x).replace('\u00A0', ' ')
  def dec2(x: Int) = dec2Fmt.format(x).replace('\u00A0', ' ')

  def short(x: Double) = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
  def short(x: Float) = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
  def short(x: Int) = (if (x > 10) thousandsFmt.format(x) else if (x > 1) dec1Fmt.format(x) else dec2Fmt.format(x)).replace('\u00A0', ' ')
}
