/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import com.mojang.authlib.GameProfile
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.{NBTTagCompound, NBTUtil}

case class DataSlotGameProfile(name: String, parent: DataSlotContainer) extends DataSlotVal[GameProfile] {
  override val default = null

  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value) = {
    if (t.hasKey(name))
      NBTUtil.readGameProfileFromNBT(t.getCompoundTag(name))
    else
      null
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    if (value != null) {
      t.setTag(name, NBTUtil.writeGameProfile(new NBTTagCompound, value))
    }
  }
}
