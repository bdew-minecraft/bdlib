package net.bdew.lib.nbt

import net.minecraft.nbt.{CompoundNBT, INBT, ListNBT}

import scala.language.implicitConversions

object NBT {

  class NBTSerialized(val value: INBT) extends AnyVal

  object NBTSerialized {
    implicit def serializeNbtPrimitive[T: Type](v: T): NBTSerialized = new NBTSerialized(Type[T].toNBT(v))

    implicit def serializeNbtList[T: Type](v: Iterable[T]): NBTSerialized = {
      val list = new ListNBT()
      for (x <- v) list.add(Type[T].toNBT(x))
      new NBTSerialized(list)
    }
  }

  def apply(pairs: (String, NBTSerialized)*): CompoundNBT = {
    val tag = new CompoundNBT()
    for ((k, v) <- pairs)
      tag.put(k, v.value)
    tag
  }

  def from(f: CompoundNBT => Unit): CompoundNBT = {
    val v = new CompoundNBT
    f(v)
    v
  }
}
