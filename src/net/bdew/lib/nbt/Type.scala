/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt

import net.minecraft.nbt._

import scala.annotation.implicitNotFound

/**
  * Base trait for types that can be stored in NBT
  */
@implicitNotFound("Type ${R} is not supported by NBT")
trait Type[R] {
  /**
    * id of underlying NBT type
    */
  val id: Int

  /**
    * Encode value to NBT type
    *
    * @return encoded value
    */
  def toNBT(p: R): NBTBase

  /**
    * Decode NBT type into value
    *
    * @return Some(decoded value) or None if wrong type
    */
  def toVal(v: NBTBase): Option[R]
}

object Type {
  def apply[R: Type] = implicitly[Type[R]]

  // Only used internally for basic types, external code should only see Type
  private abstract class TypeDef[T <: NBTBase, R](val id: Int, val cls: Class[T], val valueClass: Class[R]) extends Type[R] {
    def toVal(v: NBTBase) =
      if (cls.isInstance(v))
        Some(toValDirect(cls.cast(v)))
      else
        None

    def toValDirect(v: T): R
  }

  // Types that don't have a decoded representation
  private class TypeDefSimple[T <: NBTBase](id: Int, cls: Class[T]) extends TypeDef[T, T](id, cls, cls) {
    override def toValDirect(v: T) = v
    override def toNBT(p: T): T = p
  }

  implicit val TByte: Type[Byte] = new TypeDef(1, classOf[NBTTagByte], classOf[Byte]) {
    override def toValDirect(p: NBTTagByte): Byte = p.getByte
    override def toNBT(p: Byte): NBTTagByte = new NBTTagByte(p)
  }

  implicit val TShort: Type[Short] = new TypeDef(2, classOf[NBTTagShort], classOf[Short]) {
    override def toValDirect(p: NBTTagShort): Short = p.getShort
    override def toNBT(p: Short): NBTTagShort = new NBTTagShort(p)
  }

  implicit val TInt: Type[Int] = new TypeDef(3, classOf[NBTTagInt], classOf[Int]) {
    override def toValDirect(p: NBTTagInt): Int = p.getInt
    override def toNBT(p: Int): NBTTagInt = new NBTTagInt(p)
  }

  implicit val TLong: Type[Long] = new TypeDef(4, classOf[NBTTagLong], classOf[Long]) {
    override def toValDirect(p: NBTTagLong): Long = p.getLong
    override def toNBT(p: Long): NBTTagLong = new NBTTagLong(p)
  }

  implicit val TFloat: Type[Float] = new TypeDef(5, classOf[NBTTagFloat], classOf[Float]) {
    override def toValDirect(p: NBTTagFloat): Float = p.getFloat
    override def toNBT(p: Float): NBTTagFloat = new NBTTagFloat(p)
  }

  implicit val TDouble: Type[Double] = new TypeDef(6, classOf[NBTTagDouble], classOf[Double]) {
    override def toValDirect(p: NBTTagDouble): Double = p.getDouble
    override def toNBT(p: Double): NBTTagDouble = new NBTTagDouble(p)
  }

  implicit val TByteArray: Type[Array[Byte]] = new TypeDef(7, classOf[NBTTagByteArray], classOf[Array[Byte]]) {
    override def toValDirect(p: NBTTagByteArray): Array[Byte] = p.getByteArray
    override def toNBT(p: Array[Byte]): NBTTagByteArray = new NBTTagByteArray(p)
  }

  implicit val TString: Type[String] = new TypeDef(8, classOf[NBTTagString], classOf[String]) {
    override def toValDirect(p: NBTTagString): String = p.getString
    override def toNBT(p: String): NBTTagString = new NBTTagString(p)
  }

  implicit val TList: Type[NBTTagList] = new TypeDefSimple(9, classOf[NBTTagList])

  implicit val TCompound: Type[NBTTagCompound] = new TypeDefSimple(10, classOf[NBTTagCompound])

  implicit val TIntArray: Type[Array[Int]] = new TypeDef(11, classOf[NBTTagIntArray], classOf[Array[Int]]) {
    override def toValDirect(p: NBTTagIntArray): Array[Int] = p.getIntArray
    override def toNBT(p: Array[Int]): NBTTagIntArray = new NBTTagIntArray(p)
  }

}


