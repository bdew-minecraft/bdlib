/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.data.base.{DataSlotContainer, UpdateKind}
import net.bdew.lib.data.mixins.DataSlotOption
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.NBTTagCompound

case class DataSlotResourceKindOption(name: String, parent: DataSlotContainer) extends DataSlotOption[ResourceKind] {
  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    value foreach { r =>
      t.setTag(name, NBT.from { tag =>
        r.helperObject.saveToNBT(tag, r)
        tag.setString("kind", r.helperObject.id)
      })
    }
  }

  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value) = {
    if (t.hasKey(name)) {
      val tag = t.getCompoundTag(name)
      if (tag.hasKey("kind")) {
        ResourceManager.resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
      } else None
    } else None
  }
}
