/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt.{NBTBase, NBTTagCompound, NBTTagList}

import scala.language.implicitConversions

object NBT {

  class NBTSerialized(val value: NBTBase) extends AnyVal

  object NBTSerialized {
    implicit def serializeNbtPrimitive[T: Type](v: T): NBTSerialized = new NBTSerialized(Type[T].toNBT(v))

    implicit def serializeNbtList[T: Type](v: Traversable[T]): NBTSerialized = {
      val list = new NBTTagList
      for (x <- v) list.appendTag(Type[T].toNBT(x))
      new NBTSerialized(list)
    }
  }

  def apply(pairs: (String, NBTSerialized)*): NBTTagCompound = {
    val tag = new NBTTagCompound()
    for ((k, v) <- pairs)
      tag.setTag(k, v.value)
    tag
  }

  def from(f: (NBTTagCompound) => Unit) = {
    val v = new NBTTagCompound
    f(v)
    v
  }
}
