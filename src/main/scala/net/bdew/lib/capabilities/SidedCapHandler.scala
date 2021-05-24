package net.bdew.lib.capabilities

import net.minecraft.util.Direction
import net.minecraftforge.common.util.LazyOptional

class SidedCapHandler[T](factory: Direction => T, allowNull: Boolean = true) {
  val handlers: Map[Direction, LazyOptional[T]] = (for (side <- Direction.values.toList :+ null) yield {
    if (allowNull || side != null) {
      val handler = factory(side)
      side -> LazyOptional.of(() => handler)
    } else side -> LazyOptional.empty[T]
  }).toMap

  def get(side: Direction): LazyOptional[T] = handlers(side)
  def invalidate(): Unit = handlers.values.foreach(_.invalidate())
}
