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

object NBTTypes {
  var map = Map.empty[Int, Type[_, _]]

  @implicitNotFound("Type ${R} is not supported by NBT")
  abstract class Type[T <: NBTBase, R](val id: Int, val cls: Class[T], val valueClass: Class[R]) {
    map += id -> this
    def toVal(p: T): R
    def toNBT(p: R): T
  }

  class TypeSimple[T <: NBTBase](id: Int, cls: Class[T]) extends Type[T, T](id, cls, cls) {
    override def toVal(p: T): T = p
    override def toNBT(p: T): T = p
  }

  @implicitNotFound("Type ${R} can't currently be retrieved from NBT lists")
  case class TypeInList[T <: NBTBase, R](definition: Type[T, R], fromList: (NBTTagList, Int) => R)

  implicit object TByte extends Type(1, classOf[NBTTagByte], classOf[Byte]) {
    override def toVal(p: NBTTagByte): Byte = p.func_150290_f()
    override def toNBT(p: Byte): NBTTagByte = new NBTTagByte(p)
  }

  implicit object TShort extends Type(2, classOf[NBTTagShort], classOf[Short]) {
    override def toVal(p: NBTTagShort): Short = p.func_150289_e()
    override def toNBT(p: Short): NBTTagShort = new NBTTagShort(p)
  }

  implicit object TInt extends Type(3, classOf[NBTTagInt], classOf[Int]) {
    override def toVal(p: NBTTagInt): Int = p.func_150287_d()
    override def toNBT(p: Int): NBTTagInt = new NBTTagInt(p)
  }

  implicit object TLong extends Type(4, classOf[NBTTagLong], classOf[Long]) {
    override def toVal(p: NBTTagLong): Long = p.func_150291_c()
    override def toNBT(p: Long): NBTTagLong = new NBTTagLong(p)
  }

  implicit object TFloat extends Type(5, classOf[NBTTagFloat], classOf[Float]) {
    override def toVal(p: NBTTagFloat): Float = p.func_150288_h()
    override def toNBT(p: Float): NBTTagFloat = new NBTTagFloat(p)
  }

  implicit object TDouble extends Type(6, classOf[NBTTagDouble], classOf[Double]) {
    override def toVal(p: NBTTagDouble): Double = p.func_150286_g()
    override def toNBT(p: Double): NBTTagDouble = new NBTTagDouble(p)
  }

  implicit object TByteArray extends Type(7, classOf[NBTTagByteArray], classOf[Array[Byte]]) {
    override def toVal(p: NBTTagByteArray): Array[Byte] = p.func_150292_c()
    override def toNBT(p: Array[Byte]): NBTTagByteArray = new NBTTagByteArray(p)
  }

  implicit object TString extends Type(8, classOf[NBTTagString], classOf[String]) {
    override def toVal(p: NBTTagString): String = p.func_150285_a_()
    override def toNBT(p: String): NBTTagString = new NBTTagString(p)
  }

  implicit object TList extends TypeSimple(9, classOf[NBTTagList])

  implicit object TCompound extends TypeSimple(10, classOf[NBTTagCompound])

  implicit object TIntArray extends Type(11, classOf[NBTTagIntArray], classOf[Array[Int]]) {
    override def toVal(p: NBTTagIntArray): Array[Int] = p.func_150302_c()
    override def toNBT(p: Array[Int]): NBTTagIntArray = new NBTTagIntArray(p)
  }

  implicit val stringInList = TypeInList(TString, _.getStringTagAt(_))
  implicit val compoundInList = TypeInList(TCompound, _.getCompoundTagAt(_))
  implicit val intArrayInList = TypeInList(TIntArray, _.func_150306_c(_))
  implicit val doubleInList = TypeInList(TDouble, _.func_150309_d(_))
  implicit val floatInList = TypeInList(TFloat, _.func_150308_e(_))
}
