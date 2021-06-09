package net.bdew.lib.rich

import net.minecraftforge.common.util.LazyOptional

class RichLazyOpt[T](val o: LazyOptional[T]) extends AnyVal with IterableOnce[T] {
  override def iterator: Iterator[T] =
    o.map[Iterator[T]](Iterator(_)).orElse(Iterator.empty)

  def foreach[R](f: T => R): Unit = {
    o.ifPresent(v => f(v))
  }

  def map[R](f: T => R): LazyOptional[R] = {
    o.lazyMap(v => f(v))
  }

  def toScala: Option[T] =
    o.map[Option[T]](Some(_)).orElse(None)
}
