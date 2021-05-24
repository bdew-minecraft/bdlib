package net.bdew.lib.nbt

import net.minecraft.nbt._
import net.minecraftforge.common.util.Constants

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
  def toNBT(p: R): INBT

  /**
   * Decode NBT type into value
   *
   * @return Some(decoded value) or None if wrong type
   */
  def toVal(v: INBT): Option[R]
}

object Type {
  def apply[R: Type]: Type[R] = implicitly[Type[R]]

  // Only used internally for basic types, external code should only see Type
  private abstract class TypeDef[T <: INBT, R](val id: Int, val cls: Class[T], val valueClass: Class[R]) extends Type[R] {
    def toVal(v: INBT): Option[R] =
      if (cls.isInstance(v))
        Some(toValDirect(cls.cast(v)))
      else
        None

    def toValDirect(v: T): R
  }

  // Types that don't have a decoded representation
  private class TypeDefSimple[T <: INBT](id: Int, cls: Class[T]) extends TypeDef[T, T](id, cls, cls) {
    override def toValDirect(v: T): T = v
    override def toNBT(p: T): T = p
  }

  implicit val TByte: Type[Byte] = new TypeDef(Constants.NBT.TAG_BYTE, classOf[ByteNBT], classOf[Byte]) {
    override def toValDirect(p: ByteNBT): Byte = p.getAsByte
    override def toNBT(p: Byte): ByteNBT = ByteNBT.valueOf(p)
  }

  implicit val TShort: Type[Short] = new TypeDef(Constants.NBT.TAG_SHORT, classOf[ShortNBT], classOf[Short]) {
    override def toValDirect(p: ShortNBT): Short = p.getAsShort
    override def toNBT(p: Short): ShortNBT = ShortNBT.valueOf(p)
  }

  implicit val TInt: Type[Int] = new TypeDef(Constants.NBT.TAG_INT, classOf[IntNBT], classOf[Int]) {
    override def toValDirect(p: IntNBT): Int = p.getAsInt
    override def toNBT(p: Int): IntNBT = IntNBT.valueOf(p)
  }

  implicit val TLong: Type[Long] = new TypeDef(Constants.NBT.TAG_LONG, classOf[LongNBT], classOf[Long]) {
    override def toValDirect(p: LongNBT): Long = p.getAsLong
    override def toNBT(p: Long): LongNBT = LongNBT.valueOf(p)
  }

  implicit val TFloat: Type[Float] = new TypeDef(Constants.NBT.TAG_FLOAT, classOf[FloatNBT], classOf[Float]) {
    override def toValDirect(p: FloatNBT): Float = p.getAsFloat
    override def toNBT(p: Float): FloatNBT = FloatNBT.valueOf(p)
  }

  implicit val TDouble: Type[Double] = new TypeDef(Constants.NBT.TAG_DOUBLE, classOf[DoubleNBT], classOf[Double]) {
    override def toValDirect(p: DoubleNBT): Double = p.getAsDouble
    override def toNBT(p: Double): DoubleNBT = DoubleNBT.valueOf(p)
  }

  implicit val TByteArray: Type[Array[Byte]] = new TypeDef(Constants.NBT.TAG_BYTE_ARRAY, classOf[ByteArrayNBT], classOf[Array[Byte]]) {
    override def toValDirect(p: ByteArrayNBT): Array[Byte] = p.getAsByteArray
    override def toNBT(p: Array[Byte]): ByteArrayNBT = new ByteArrayNBT(p)
  }

  implicit val TString: Type[String] = new TypeDef(Constants.NBT.TAG_STRING, classOf[StringNBT], classOf[String]) {
    override def toValDirect(p: StringNBT): String = p.getAsString
    override def toNBT(p: String): StringNBT = StringNBT.valueOf(p)
  }

  implicit val TList: Type[ListNBT] = new TypeDefSimple(Constants.NBT.TAG_LIST, classOf[ListNBT])

  implicit val TCompound: Type[CompoundNBT] = new TypeDefSimple(Constants.NBT.TAG_COMPOUND, classOf[CompoundNBT])

  implicit val TIntArray: Type[Array[Int]] = new TypeDef(Constants.NBT.TAG_INT_ARRAY, classOf[IntArrayNBT], classOf[Array[Int]]) {
    override def toValDirect(p: IntArrayNBT): Array[Int] = p.getAsIntArray
    override def toNBT(p: Array[Int]): IntArrayNBT = new IntArrayNBT(p)
  }

  implicit val TLongArray: Type[Array[Long]] = new TypeDef(Constants.NBT.TAG_LONG_ARRAY, classOf[LongArrayNBT], classOf[Array[Long]]) {
    override def toValDirect(p: LongArrayNBT): Array[Long] = p.getAsLongArray
    override def toNBT(p: Array[Long]): LongArrayNBT = new LongArrayNBT(p)
  }
}


