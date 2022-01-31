package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.core.Direction

object TDirection extends ConvertedType[Direction, Byte] {
  override def encode(v: Direction): Byte = v.ordinal().toByte
  override def decode(v: Byte): Option[Direction] = Some(Direction.values()(v))
}
