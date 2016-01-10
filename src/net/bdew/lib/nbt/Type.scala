/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt._

import scala.annotation.implicitNotFound

@implicitNotFound("Type ${R} is not supported by NBT")
trait Type[R] {
  def toNBT(p: R): NBTBase
  def fromCompound(t: NBTTagCompound, n: String): Option[R]
}

object Type {
  def apply[R: Type] = implicitly[Type[R]]

  implicit object TByte extends TypeDef(1, classOf[NBTTagByte], classOf[Byte]) {
    override def toVal(p: NBTTagByte): Byte = p.getByte
    override def toNBT(p: Byte): NBTTagByte = new NBTTagByte(p)
  }

  implicit object TShort extends TypeDef(2, classOf[NBTTagShort], classOf[Short]) {
    override def toVal(p: NBTTagShort): Short = p.getShort
    override def toNBT(p: Short): NBTTagShort = new NBTTagShort(p)
  }

  implicit object TInt extends TypeDef(3, classOf[NBTTagInt], classOf[Int]) {
    override def toVal(p: NBTTagInt): Int = p.getInt
    override def toNBT(p: Int): NBTTagInt = new NBTTagInt(p)
  }

  implicit object TLong extends TypeDef(4, classOf[NBTTagLong], classOf[Long]) {
    override def toVal(p: NBTTagLong): Long = p.getLong
    override def toNBT(p: Long): NBTTagLong = new NBTTagLong(p)
  }

  implicit object TFloat extends TypeDef(5, classOf[NBTTagFloat], classOf[Float]) {
    override def toVal(p: NBTTagFloat): Float = p.getFloat
    override def toNBT(p: Float): NBTTagFloat = new NBTTagFloat(p)
  }

  implicit object TDouble extends TypeDef(6, classOf[NBTTagDouble], classOf[Double]) {
    override def toVal(p: NBTTagDouble): Double = p.getDouble
    override def toNBT(p: Double): NBTTagDouble = new NBTTagDouble(p)
  }

  implicit object TByteArray extends TypeDef(7, classOf[NBTTagByteArray], classOf[Array[Byte]]) {
    override def toVal(p: NBTTagByteArray): Array[Byte] = p.getByteArray
    override def toNBT(p: Array[Byte]): NBTTagByteArray = new NBTTagByteArray(p)
  }

  implicit object TString extends TypeDef(8, classOf[NBTTagString], classOf[String]) {
    override def toVal(p: NBTTagString): String = p.getString
    override def toNBT(p: String): NBTTagString = new NBTTagString(p)
  }

  implicit object TList extends TypeDefSimple(9, classOf[NBTTagList])

  implicit object TCompound extends TypeDefSimple(10, classOf[NBTTagCompound])

  implicit object TIntArray extends TypeDef(11, classOf[NBTTagIntArray], classOf[Array[Int]]) {
    override def toVal(p: NBTTagIntArray): Array[Int] = p.getIntArray
    override def toNBT(p: Array[Int]): NBTTagIntArray = new NBTTagIntArray(p)
  }
}


