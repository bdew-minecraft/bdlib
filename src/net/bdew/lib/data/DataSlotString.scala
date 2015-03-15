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

case class DataSlotString(name: String, parent: DataSlotContainer, default: String = null) extends DataSlotVal[String] {
  var value: String = default
  def save(t: NBTTagCompound, kind: UpdateKind.Value) = if (value != null) t.setString(name, value)
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = value = if (t.hasKey(name)) t.getString(name) else null
}
