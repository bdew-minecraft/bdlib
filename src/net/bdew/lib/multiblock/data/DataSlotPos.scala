/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.DataSlotOption
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotPos(name: String, parent: TileDataSlots) extends DataSlotOption[BlockRef] {
  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD)

  def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    cval map (x => t.setTag(name, Misc.applyMutator(x.writeToNBT, new NBTTagCompound)))
  }

  def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    cval = if (t.hasKey(name))
      Some(BlockRef.fromNBT(t.getCompoundTag(name)))
    else
      None
  }
}
