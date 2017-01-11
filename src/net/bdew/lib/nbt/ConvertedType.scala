/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt.NBTBase

/**
  * Definition of type that can be converted to something NBT-compatible
  *
  * @tparam T type that this object supports
  * @tparam R underlying type
  */
abstract class ConvertedType[T, R: Type] extends Type[T] {
  val encodedType = Type[R]

  override val id: Int = encodedType.id

  def encode(v: T): R
  def decode(v: R): Option[T]

  override def toNBT(p: T) = encodedType.toNBT(encode(p))
  override def toVal(v: NBTBase) = encodedType.toVal(v) flatMap decode
}











