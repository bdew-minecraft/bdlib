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
  def toNBT(p: R): Tag

  /**
   * Decode NBT type into value
   *
   * @return Some(decoded value) or None if wrong type
   */
  def toVal(v: Tag): Option[R]
}

object Type {
  def apply[R: Type]: Type[R] = implicitly[Type[R]]

  // Only used internally for basic types, external code should only see Type
  private abstract class TypeDef[T <: Tag, R](val id: Int, val cls: Class[T], val valueClass: Class[R]) extends Type[R] {
    def toVal(v: Tag): Option[R] =
      if (cls.isInstance(v))
        Some(toValDirect(cls.cast(v)))
      else
        None

    def toValDirect(v: T): R
  }

  // Types that don't have a decoded representation
  private class TypeDefSimple[T <: Tag](id: Int, cls: Class[T]) extends TypeDef[T, T](id, cls, cls) {
    override def toValDirect(v: T): T = v
    override def toNBT(p: T): T = p
  }

  implicit val TByte: Type[Byte] = new TypeDef(Tag.TAG_BYTE, classOf[ByteTag], classOf[Byte]) {
    override def toValDirect(p: ByteTag): Byte = p.getAsByte
    override def toNBT(p: Byte): ByteTag = ByteTag.valueOf(p)
  }

  implicit val TShort: Type[Short] = new TypeDef(Tag.TAG_SHORT, classOf[ShortTag], classOf[Short]) {
    override def toValDirect(p: ShortTag): Short = p.getAsShort
    override def toNBT(p: Short): ShortTag = ShortTag.valueOf(p)
  }

  implicit val TInt: Type[Int] = new TypeDef(Tag.TAG_INT, classOf[IntTag], classOf[Int]) {
    override def toValDirect(p: IntTag): Int = p.getAsInt
    override def toNBT(p: Int): IntTag = IntTag.valueOf(p)
  }

  implicit val TLong: Type[Long] = new TypeDef(Tag.TAG_LONG, classOf[LongTag], classOf[Long]) {
    override def toValDirect(p: LongTag): Long = p.getAsLong
    override def toNBT(p: Long): LongTag = LongTag.valueOf(p)
  }

  implicit val TFloat: Type[Float] = new TypeDef(Tag.TAG_FLOAT, classOf[FloatTag], classOf[Float]) {
    override def toValDirect(p: FloatTag): Float = p.getAsFloat
    override def toNBT(p: Float): FloatTag = FloatTag.valueOf(p)
  }

  implicit val TDouble: Type[Double] = new TypeDef(Tag.TAG_DOUBLE, classOf[DoubleTag], classOf[Double]) {
    override def toValDirect(p: DoubleTag): Double = p.getAsDouble
    override def toNBT(p: Double): DoubleTag = DoubleTag.valueOf(p)
  }

  implicit val TByteArray: Type[Array[Byte]] = new TypeDef(Tag.TAG_BYTE_ARRAY, classOf[ByteArrayTag], classOf[Array[Byte]]) {
    override def toValDirect(p: ByteArrayTag): Array[Byte] = p.getAsByteArray
    override def toNBT(p: Array[Byte]): ByteArrayTag = new ByteArrayTag(p)
  }

  implicit val TString: Type[String] = new TypeDef(Tag.TAG_STRING, classOf[StringTag], classOf[String]) {
    override def toValDirect(p: StringTag): String = p.getAsString
    override def toNBT(p: String): StringTag = StringTag.valueOf(p)
  }

  implicit val TList: Type[ListTag] = new TypeDefSimple(Tag.TAG_LIST, classOf[ListTag])

  implicit val TCompound: Type[CompoundTag] = new TypeDefSimple(Tag.TAG_COMPOUND, classOf[CompoundTag])

  implicit val TIntArray: Type[Array[Int]] = new TypeDef(Tag.TAG_INT_ARRAY, classOf[IntArrayTag], classOf[Array[Int]]) {
    override def toValDirect(p: IntArrayTag): Array[Int] = p.getAsIntArray
    override def toNBT(p: Array[Int]): IntArrayTag = new IntArrayTag(p)
  }

  implicit val TLongArray: Type[Array[Long]] = new TypeDef(Tag.TAG_LONG_ARRAY, classOf[LongArrayTag], classOf[Array[Long]]) {
    override def toValDirect(p: LongArrayTag): Array[Long] = p.getAsLongArray
    override def toNBT(p: Array[Long]): LongArrayTag = new LongArrayTag(p)
  }
}


