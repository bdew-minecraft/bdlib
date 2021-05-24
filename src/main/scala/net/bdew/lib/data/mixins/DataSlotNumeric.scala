package net.bdew.lib.data.mixins

/**
 * Base class for data slots that hold numeric values
 * adds convenience operators to modify the value
 */

trait DataSlotNumeric[T] extends DataSlotOrdered[T] {
  def numeric: Numeric[T]
  override def ordering: Ordering[T] = numeric

  def +=(that: T): Unit = update(numeric.plus(value, that))
  def -=(that: T): Unit = update(numeric.minus(value, that))
  def *=(that: T): Unit = update(numeric.times(value, that))

  /* Special Int versions, using the conversion from numeric */
  def +=(that: Int): Unit = update(numeric.plus(value, numeric.fromInt(that)))
  def -=(that: Int): Unit = update(numeric.minus(value, numeric.fromInt(that)))
  def *=(that: Int): Unit = update(numeric.times(value, numeric.fromInt(that)))
}
