package net.bdew.lib.nbt

import net.minecraft.nbt.INBT

/**
 * Definition of type that can be converted to something NBT-compatible
 *
 * @tparam T type that this object supports
 * @tparam R underlying type
 */
abstract class ConvertedType[T, R: Type] extends Type[T] {
  val encodedType: Type[R] = Type[R]

  override val id: Int = encodedType.id

  def encode(v: T): R
  def decode(v: R): Option[T]

  override def toNBT(p: T): INBT = encodedType.toNBT(encode(p))
  override def toVal(v: INBT): Option[T] = encodedType.toVal(v) flatMap decode
}











