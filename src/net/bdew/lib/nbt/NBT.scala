/*
 * Copyright (c) bdew, 2013 - 2015
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

  implicit def serializeNbtPrimitive[T: Type](v: T): NBTSerialized = new NBTSerialized(Type[T].toNBT(v))

  implicit def serializeNbtList[T: Type](v: List[T]): NBTSerialized = {
    val list = new NBTTagList
    for (x <- v) list.appendTag(Type[T].toNBT(x))
    new NBTSerialized(list)
  }

  def apply(pairs: (String, NBTSerialized)*): NBTTagCompound = apply(pairs.toMap)

  def apply(m: Map[String, NBTSerialized]): NBTTagCompound = {
    val tag = new NBTTagCompound()
    for ((k, v) <- m)
      tag.setTag(k, v.value)
    tag
  }
}
