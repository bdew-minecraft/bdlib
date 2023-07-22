package net.bdew.lib.data.mixins

import net.bdew.lib.data.base.DataSlotVal

/**
 * Base class for data slots that hold values with ordering operators, adds more convenience operators
 */
trait DataSlotOrdered[T] extends DataSlotVal[T] {
  def ordering: Ordering[T]
}
