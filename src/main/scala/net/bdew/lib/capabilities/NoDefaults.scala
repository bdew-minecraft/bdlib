package net.bdew.lib.capabilities

import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability

import java.util.concurrent.Callable
import scala.reflect.ClassTag

/**
 * Null implementation of capability storage, for use when providing an implementation makes no sense
 */
class NoStorage[T] extends Capability.IStorage[T] {
  override def writeNBT(capability: Capability[T], instance: T, side: Direction): INBT =
    throw new UnsupportedOperationException("Default capability storage for %s should not be used".format(capability.getName))

  override def readNBT(capability: Capability[T], instance: T, side: Direction, nbt: INBT): Unit =
    throw new UnsupportedOperationException("Default capability storage for %s should not be used".format(capability.getName))
}

/**
 * Null implementation of capability factory, for use when providing an implementation makes no sense
 */
class NoFactory[T: ClassTag] extends Callable[T] {
  override def call(): T =
    throw new UnsupportedOperationException("Default capability factory for %s should not be used".format(implicitly[ClassTag[T]].runtimeClass.getName))
}