/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.mixins

/**
  * Base class for data slots that hold numeric values
  * adds convenience operators to modify the value
  */

trait DataSlotNumeric[T] extends DataSlotOrdered[T] {
  def numeric: Numeric[T]
  override def ordering = numeric

  def +=(that: T) = update(numeric.plus(value, that))
  def -=(that: T) = update(numeric.minus(value, that))
  def *=(that: T) = update(numeric.times(value, that))

  /* Special Int versions, using the conversion from numeric */
  def +=(that: Int) = update(numeric.plus(value, numeric.fromInt(that)))
  def -=(that: Int) = update(numeric.minus(value, numeric.fromInt(that)))
  def *=(that: Int) = update(numeric.times(value, numeric.fromInt(that)))
}
