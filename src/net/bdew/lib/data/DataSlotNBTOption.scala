/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.nbt.Type
import net.minecraft.nbt.NBTTagCompound

abstract class DataSlotNBTOption[T: Type] extends DataSlotOption[T] {
  override def save(t: NBTTagCompound, kind: UpdateKind.Value) =
    value foreach (v => t.set(name, v))

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) =
    value = t.get[T](name)
}
