package net.bdew.lib.capabilities

import java.util.concurrent.Callable
import scala.reflect.ClassTag

/**
 * Null implementation of capability factory, for use when providing an implementation makes no sense
 */
class NoFactory[T: ClassTag] extends Callable[T] {
  override def call(): T =
    throw new UnsupportedOperationException("Default capability factory for %s should not be used".format(implicitly[ClassTag[T]].runtimeClass.getName))
}