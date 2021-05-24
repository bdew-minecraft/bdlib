package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType

object TBoolean extends ConvertedType[Boolean, Byte] {
  override def encode(v: Boolean): Byte = if (v) 1.toByte else 0.toByte
  override def decode(v: Byte): Option[Boolean] = Some(v > 0.toByte)
}
