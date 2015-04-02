/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt.{NBTBase, NBTTagCompound}

import scala.annotation.implicitNotFound

@implicitNotFound("Type ${R} is not supported by NBT")
abstract class TypeDef[T <: NBTBase, R](val id: Int, val cls: Class[T], val valueClass: Class[R]) extends Type[R] {
  TypeDef.map += id -> this
  def toVal(p: T): R
  def toNBT(p: R): T
  override def fromCompound(t: NBTTagCompound, n: String): Option[R] =
    if (t.hasKey(n, id)) Some(toVal(t.getTag(n).asInstanceOf[T])) else None
}

object TypeDef {
  var map = Map.empty[Int, TypeDef[_, _]]
}

class TypeDefSimple[T <: NBTBase](id: Int, cls: Class[T]) extends TypeDef[T, T](id, cls, cls) {
  override def toVal(p: T): T = p
  override def toNBT(p: T): T = p
}
