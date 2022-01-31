package net.bdew.lib.nbt

import net.minecraft.nbt.{CompoundTag, ListTag, Tag}

import scala.language.implicitConversions

object NBT {

  class NBTSerialized(val value: Tag) extends AnyVal

  object NBTSerialized {
    implicit def serializeNbtPrimitive[T: Type](v: T): NBTSerialized = new NBTSerialized(Type[T].toNBT(v))

    implicit def serializeNbtList[T: Type](v: Iterable[T]): NBTSerialized = {
      val list = new ListTag()
      for (x <- v) list.add(Type[T].toNBT(x))
      new NBTSerialized(list)
    }
  }

  def apply(pairs: (String, NBTSerialized)*): CompoundTag = {
    val tag = new CompoundTag()
    for ((k, v) <- pairs)
      tag.put(k, v.value)
    tag
  }

  def from(f: CompoundTag => Unit): CompoundTag = {
    val v = new CompoundTag
    f(v)
    v
  }
}
