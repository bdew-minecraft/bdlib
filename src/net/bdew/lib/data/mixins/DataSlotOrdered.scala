/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.mixins

import net.bdew.lib.data.base.DataSlotVal

/**
  * Base class for data slots that hold values with ordering operators, adds more convenience operators
  * TODO: Do i need this? normal operators can be used with the implicit conversion
  */
trait DataSlotOrdered[T] extends DataSlotVal[T] {
  def ordering: Ordering[T]
  def :>(that: T) = ordering.compare(value, that) > 0
  def :<(that: T) = ordering.compare(value, that) < 0
  def :>=(that: T) = ordering.compare(value, that) >= 0
  def :<=(that: T) = ordering.compare(value, that) <= 0
}
