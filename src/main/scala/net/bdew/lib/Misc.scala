package net.bdew.lib

import com.google.common.collect.ImmutableList
import net.minecraftforge.fml.ModLoadingContext

import scala.util.DynamicVariable

object Misc {
  private val forcedModId = new DynamicVariable[Option[String]](None)

  def withModId[R](s: String)(f: => R): R = forcedModId.withValue(Some(s))(f)

  def getActiveModId: String =
    forcedModId.value.orElse(Option(ModLoadingContext.get().getActiveContainer.getModId)) getOrElse "<UNKNOWN>"

  def filterType[T](from: Iterable[_], cls: Class[T]): Iterable[T] = from.filter(cls.isInstance).asInstanceOf[Iterable[T]]

  def min[T: Ordering](values: T*): T = values.min
  def max[T: Ordering](values: T*): T = values.max

  def clamp[T](value: T, min: T, max: T)(implicit o: Ordering[T]): T =
    if (o.gt(value, max)) max else if (o.lt(value, min)) min else value

  // Easy replacement for various "if(foo.isInstanceOf[Bar]) foo.asInstanceOf[Bar]" constructs
  def asInstanceOpt[T](v: Any, cls: Class[T]): Option[T] =
    if (cls.isInstance(v)) Some(v.asInstanceOf[T]) else None

  def nextInSeq[T](seq: Seq[T], elem: T): T = {
    val pos = seq.indexOf(elem)
    if (pos < 0 || pos == seq.size - 1)
      seq.head
    else
      seq(pos + 1)
  }

  def prevInSeq[T](seq: Seq[T], elem: T): T = {
    val pos = seq.indexOf(elem)
    if (pos <= 0)
      seq.last
    else
      seq(pos - 1)
  }

  def jImmutable[V](i: Iterable[V]): ImmutableList[V] = {
    import scala.jdk.CollectionConverters.IterableHasAsJava
    ImmutableList.copyOf(i.asJava)
  }

  def transformMapValues[K, V, R](map: Map[K, V], transform: V => R): Map[K, R] =
    map.map(x => x._1 -> transform(x._2))
}
