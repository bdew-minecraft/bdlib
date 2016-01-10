/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.bdew.lib.nbt.Type.TCompound
import net.bdew.lib.nbt.{ListElement, NBT, Type}
import net.bdew.lib.rich.{RichBlockAccess, RichBlockPos, RichNBTTagCompound, RichWorld}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.{IBlockAccess, World}

import scala.language.implicitConversions

object PimpVanilla {
  implicit def pimpBlockPos(p: BlockPos): RichBlockPos = new RichBlockPos(p)
  implicit def pimpWorld(p: World): RichWorld = new RichWorld(p)
  implicit def pimpBlockAccess(p: IBlockAccess): RichBlockAccess = new RichBlockAccess(p)
  implicit def pimpNBT(p: NBTTagCompound): RichNBTTagCompound = new RichNBTTagCompound(p)

  // Helpers for NBT stuff

  implicit object TBlockPos extends Type[BlockPos] {
    def toVal(t: NBTTagCompound) = new BlockPos(t.getInteger("x"), t.getInteger("y"), t.getInteger("z"))

    override def fromCompound(t: NBTTagCompound, n: String) = {
      if (t.hasKey(n, TCompound.id)) {
        Some(toVal(t.getCompoundTag(n)))
      } else None
    }

    override def toNBT(p: BlockPos) =
      NBT(
        "x" -> p.getX,
        "y" -> p.getY,
        "z" -> p.getZ
      )
  }

  implicit val blockPosListItem = ListElement(TCompound.id, TBlockPos, (list, pos) => TBlockPos.toVal(list.getCompoundTagAt(pos)))
}
