/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.bdew.lib.nbt.{ListElement, Type}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

import scala.language.implicitConversions

class RichNBTTagCompound(val tag: NBTTagCompound) extends AnyVal {

  def getList[T: ListElement](name: String): Iterable[T] = {
    val tDef = ListElement[T]
    val list = tag.getTagList(name, tDef.typeId)
    for (i <- 0 until list.tagCount()) yield tDef.fromList(list, i)
  }

  def setList[T: Type](name: String, v: Iterable[T]): Unit = {
    val list = new NBTTagList
    for (x <- v) list.appendTag(Type[T].toNBT(x))
    tag.setTag(name, list)
  }

  def toItemStack = Option(ItemStack.loadItemStackFromNBT(tag))

  def get[T: Type](name: String): Option[T] = {
    implicitly[Type[T]].fromCompound(tag, name)
  }

  def get[T: Type](name: String, default: => T): T = {
    implicitly[Type[T]].fromCompound(tag, name).getOrElse(default)
  }

  def set[T: Type](name: String, value: T): Unit = {
    tag.setTag(name, implicitly[Type[T]].toNBT(value))
  }
}