package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.Type.TInt
import net.bdew.lib.nbt.{ConvertedType, NBT}
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag

object TBlockPos extends ConvertedType[BlockPos, CompoundTag] {
  override def encode(v: BlockPos): CompoundTag =
    NBT(
      "x" -> v.getX,
      "y" -> v.getY,
      "z" -> v.getZ
    )

  override def decode(t: CompoundTag): Option[BlockPos] =
    if (t.contains("x", TInt.id) && t.contains("y", TInt.id) && t.contains("z", TInt.id))
      Some(new BlockPos(t.getInt("x"), t.getInt("y"), t.getInt("z")))
    else
      None
}
