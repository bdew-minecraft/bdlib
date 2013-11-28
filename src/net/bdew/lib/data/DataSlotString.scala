/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.minecraft.nbt.NBTTagCompound
import net.bdew.lib.data.base.{UpdateKind, TileDataSlots, DataSlotVal}

case class DataSlotString(name: String, parent: TileDataSlots, default: String = null) extends DataSlotVal[String] {
  var cval: String = default
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = if (cval != null) t.setString(name, cval)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = cval = if (t.hasKey(name)) t.getString(name) else null
}
