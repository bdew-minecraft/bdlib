/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotNumeric, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotInt(name: String, parent: TileDataSlots, default: Int = 0) extends DataSlotNumeric[Int](default) {
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = t.setInteger(name, cval)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = cval = t.getInteger(name)
}
