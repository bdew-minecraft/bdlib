/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.bdew.lib.nbt.Type
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

import scala.language.implicitConversions

class RichNBTTagCompound(val tag: NBTTagCompound) extends AnyVal {

  def getList[T: Type](name: String): Iterable[T] = {
    val vtype = Type[T]
    val list = tag.getTagList(name, vtype.id)
    for (i <- 0 until list.tagCount(); element <- vtype.toVal(list.get(i))) yield element
  }

  def setList[T: Type](name: String, v: Iterable[T]): Unit = {
    val list = new NBTTagList
    for (x <- v) list.appendTag(Type[T].toNBT(x))
    tag.setTag(name, list)
  }

  def toItemStack = Some(new ItemStack(tag))

  def get[T: Type](name: String): Option[T] = {
    val vtype = Type[T]
    Option(tag.getTag(name)) flatMap (v => vtype.toVal(v))
  }

  def get[T: Type](name: String, default: => T): T =
    get(name).getOrElse(default)

  def set[T: Type](name: String, value: T): Unit = {
    tag.setTag(name, Type[T].toNBT(value))
  }
}