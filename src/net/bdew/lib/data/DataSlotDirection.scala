/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotVal, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection

case class DataSlotDirection(name: String, parent: TileDataSlots) extends DataSlotVal[ForgeDirection] {
  var cval = ForgeDirection.UP
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = t.setByte(name, cval.ordinal().toByte)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = cval = ForgeDirection.getOrientation(t.getByte(name))
}
