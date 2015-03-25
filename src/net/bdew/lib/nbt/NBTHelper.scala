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

object NBTHelper {

  import NBTTypes._

  class RichNBTTagCompound(x: NBTTagCompound) {
    def getList[T](name: String)(implicit ev: TypeInList[_, T]): Iterable[T] = {
      val list = x.getTagList(name, ev.definition.id)
      for (i <- 0 until list.tagCount()) yield ev.fromList(list, i)
    }
    def setList[T](name: String, v: Iterable[T])(implicit ev: Type[_ <: NBTBase, T]): Unit = {
      val list = new NBTTagList
      for (x <- v) list.appendTag(ev.toNBT(x))
    }
  }

  implicit def compound2rich(p: NBTTagCompound): RichNBTTagCompound = new RichNBTTagCompound(p)
}
