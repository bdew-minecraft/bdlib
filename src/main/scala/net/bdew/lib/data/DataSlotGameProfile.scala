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
import net.minecraft.nbt.{CompoundNBT, NBTUtil}
import net.minecraftforge.common.util.Constants

case class DataSlotGameProfile(name: String, parent: DataSlotContainer) extends DataSlotVal[GameProfile] {
  override val default: GameProfile = null

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): GameProfile = {
    if (t.contains(name, Constants.NBT.TAG_COMPOUND))
      NBTUtil.readGameProfile(t.getCompound(name))
    else
      null
  }

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    if (value != null) {
      t.put(name, NBTUtil.writeGameProfile(new CompoundNBT, value))
    }
  }
}
