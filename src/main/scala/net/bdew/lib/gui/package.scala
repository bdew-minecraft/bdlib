package net.bdew.lib

package object gui {
  type Rect = BaseRect[Float]
  type Point = BasePoint[Float]

  import scala.language.implicitConversions

  implicit def awt2point(p: java.awt.Point): BasePoint[Int] = BasePoint(p.x, p.y)
  implicit def point2awt(p: BasePoint[Int]): java.awt.Point = new java.awt.Point(p.x, p.y)
  implicit def tuple2point[T: Numeric](p: (T, T)): BasePoint[T] = BasePoint(p._1, p._2)

  // Converts different kinds of Point/Rect as long as there's an implicit for the underlying type

  implicit def pointConvert[T, R](a: BasePoint[R])(implicit f: R => T, n: Numeric[T]): BasePoint[T] =
    BasePoint[T](f(a.x), f(a.y))

  implicit def rectConvert[T, R](a: BaseRect[R])(implicit f: R => T, n: Numeric[T]): BaseRect[T] =
    BaseRect[T](f(a.x), f(a.y), f(a.w), f(a.h))

  def Rect(x: Float, y: Float, w: Float, h: Float): BaseRect[Float] = BaseRect(x, y, w, h)
  def Rect(x: Int, y: Int, w: Int, h: Int): BaseRect[Float] = BaseRect(x.toFloat, y.toFloat, w.toFloat, h.toFloat)
  def Rect(x: Double, y: Double, w: Double, h: Double): BaseRect[Float] = BaseRect(x.toFloat, y.toFloat, w.toFloat, h.toFloat)

  def Point(x: Float, y: Float): BasePoint[Float] = BasePoint(x, y)
  def Point(x: Int, y: Int): BasePoint[Float] = BasePoint(x.toFloat, y.toFloat)
  def Point(x: Double, y: Double): BasePoint[Float] = BasePoint(x.toFloat, y.toFloat)
}

