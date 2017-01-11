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
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos

object TBlockPos extends ConvertedType[BlockPos, NBTTagCompound] {
  override def encode(v: BlockPos) =
    NBT(
      "x" -> v.getX,
      "y" -> v.getY,
      "z" -> v.getZ
    )

  override def decode(t: NBTTagCompound) =
    if (t.hasKey("x", TInt.id) && t.hasKey("y", TInt.id) && t.hasKey("z", TInt.id))
      Some(new BlockPos(t.getInteger("x"), t.getInteger("y"), t.getInteger("z")))
    else
      None
}
