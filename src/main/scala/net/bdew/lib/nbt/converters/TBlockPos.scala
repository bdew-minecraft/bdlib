/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.Type.TInt
import net.bdew.lib.nbt.{ConvertedType, NBT}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos

object TBlockPos extends ConvertedType[BlockPos, CompoundNBT] {
  override def encode(v: BlockPos): CompoundNBT =
    NBT(
      "x" -> v.getX,
      "y" -> v.getY,
      "z" -> v.getZ
    )

  override def decode(t: CompoundNBT): Option[BlockPos] =
    if (t.contains("x", TInt.id) && t.contains("y", TInt.id) && t.contains("z", TInt.id))
      Some(new BlockPos(t.getInt("x"), t.getInt("y"), t.getInt("z")))
    else
      None
}
