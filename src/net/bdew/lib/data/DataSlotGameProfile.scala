/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import java.util.UUID

import com.mojang.authlib.GameProfile
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.lang3.StringUtils

case class DataSlotGameProfile(name: String, parent: DataSlotContainer) extends DataSlotVal[GameProfile] {
  override var value: GameProfile = null

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) = {
    value = if (t.hasKey(name + "_uid") || t.hasKey(name + "_name")) {
      val uId = if (t.hasKey(name + "_uid")) UUID.fromString(t.getString(name + "_uid")) else null
      val uName = if (t.hasKey(name + "_name")) t.getString(name + "_name") else null
      new GameProfile(uId, uName)
    } else null
  }

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    if (value != null) {
      Option(value.getId) foreach (id => t.setString(name + "_uid", id.toString))
      Option(value.getName) filterNot (name => StringUtils.isBlank(name)) foreach (name => t.setString(name + "_name", name))
    }
  }
}
