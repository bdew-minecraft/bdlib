/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection

case class DataSlotDirection(name: String, parent: DataSlotContainer) extends DataSlotVal[ForgeDirection] {
  var value = ForgeDirection.UP
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = t.setByte(name, value.ordinal().toByte)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = value = ForgeDirection.getOrientation(t.getByte(name))
}
