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
 * Base class for data slots that hold values with ordering operators, adds more convenience operators
 * TODO: Do i need this? normal operators can be used with the implicit conversion
 */
abstract class DataSlotOrdered[T](implicit n: Ordering[T]) extends DataSlotVal[T] {
  def :>(that: T) = n.compare(value, that) > 0
  def :<(that: T) = n.compare(value, that) < 0
  def :>=(that: T) = n.compare(value, that) >= 0
  def :<=(that: T) = n.compare(value, that) <= 0
}
