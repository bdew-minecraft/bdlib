/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

/**
 * Base class for data slots that hold numeric values
 * adds convenience operators to modify the value
 */

abstract class DataSlotNumeric[T](default: T)(implicit n: Numeric[T]) extends DataSlotVal[T] {
  var value = default
  def +=(that: T) = update(n.plus(value, that))
  def -=(that: T) = update(n.minus(value, that))
  def *=(that: T) = update(n.times(value, that))

  /* Special Int versions, using the conversion from numeric */
  def +=(that: Int) = update(n.plus(value, n.fromInt(that)))
  def -=(that: Int) = update(n.minus(value, n.fromInt(that)))
  def *=(that: Int) = update(n.times(value, n.fromInt(that)))
}
