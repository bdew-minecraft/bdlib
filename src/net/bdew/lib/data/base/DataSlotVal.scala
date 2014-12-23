/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import scala.language.implicitConversions

/**
 * Base trait for data slots that hold primitive values
 * Defines some convenience operators to access the value and an implicit conversion to the value
 */

trait DataSlotVal[T] extends DataSlot {
  var value: T

  def isSame(v: T) = v == value

  def :=(that: T) = update(that)

  def :!=(that: T) = value != that
  def :==(that: T) = value == that

  def update(v: T) {
    if (!isSame(v)) {
      value = v
      parent.dataSlotChanged(this)
    }
  }
}

object DataSlotVal {
  implicit def slot2val[T](slot: DataSlotVal[T]): T = slot.value
}