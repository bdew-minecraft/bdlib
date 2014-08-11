/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.data

import java.util.UUID

import com.mojang.authlib.GameProfile
import net.bdew.lib.data.base.{DataSlotVal, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.lang3.StringUtils

case class DataSlotGameProfile(name: String, parent: TileDataSlots) extends DataSlotVal[GameProfile] {
  override var cval: GameProfile = null

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) = {
    cval = if (t.hasKey(name + "_uid") || t.hasKey(name + "_name")) {
      val uid = if (t.hasKey(name + "_uid")) UUID.fromString(t.getString(name + "_uid")) else null
      val uname = if (t.hasKey(name + "_name")) t.getString(name + "_name") else null
      new GameProfile(uid, uname)
    } else null
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    if (cval != null) {
      Option(cval.getId) map (id => t.setString(name + "_uid", id.toString))
      Option(cval.getName) filterNot (name => StringUtils.isBlank(name)) map (name => t.setString(name + "_name", name))
    }
  }
}
