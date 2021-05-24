package net.bdew.lib.gui

case class BasePoint[T: Numeric](x: T, y: T) {
  val n: Numeric[T] = implicitly[Numeric[T]]

  import n.mkNumericOps

  def this(p: BasePoint[T]) = this(p.x, p.y)

  def +(that: BasePoint[T]): BasePoint[T] = BasePoint(this.x + that.x, this.y + that.y)
  def +(xOff: T, yOff: T): BasePoint[T] = BasePoint(this.x + xOff, this.y + yOff)
  def -(that: BasePoint[T]): BasePoint[T] = BasePoint(this.x - that.x, this.y - that.y)
  def -(xOff: T, yOff: T): BasePoint[T] = BasePoint(this.x - xOff, this.y - yOff)

  def map[R: Numeric](f: T => R): BasePoint[R] = BasePoint(f(x), f(y))
}
