/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data.base

import scala.language.implicitConversions

/**
 * Base trait for data slots that hold primitive values
 * Defines some convinience operators to access the value and an implicit conversion to the value
 */

trait DataSlotVal[T] extends DataSlot {
  var cval: T

  def isSame(v: T) = v == cval

  def :=(that: T) = update(that)

  def :!=(that: T) = cval != that
  def :==(that: T) = cval == that

  def update(v: T) {
    if (!isSame(v)) {
      cval = v
      parent.dataSlotChanged(this)
    }
  }
}

object DataSlotVal {
  implicit def slot2val[T](slot: DataSlotVal[T]): T = slot.cval
}