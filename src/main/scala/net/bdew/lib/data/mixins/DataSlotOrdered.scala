package net.bdew.lib.data.mixins

import net.bdew.lib.data.base.DataSlotVal

/**
 * Base class for data slots that hold values with ordering operators, adds more convenience operators
 * TODO: Do i need this? normal operators can be used with the implicit conversion
 */
trait DataSlotOrdered[T] extends DataSlotVal[T] {
  def ordering: Ordering[T]
  def :>(that: T): Boolean = ordering.compare(value, that) > 0
  def :<(that: T): Boolean = ordering.compare(value, that) < 0
  def :>=(that: T): Boolean = ordering.compare(value, that) >= 0
  def :<=(that: T): Boolean = ordering.compare(value, that) <= 0
}
