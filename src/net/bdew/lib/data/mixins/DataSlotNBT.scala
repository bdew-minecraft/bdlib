/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.mixins

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.bdew.lib.nbt.Type
import net.minecraft.nbt.NBTTagCompound

trait DataSlotNBT[T] extends DataSlotVal[T] {
  implicit def nbtType: Type[T]
  override def save(t: NBTTagCompound, kind: UpdateKind.Value) =
    if (value != null)
      t.set(name, value)
  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value) =
    t.get[T](name).getOrElse(default)
}

trait DataSlotNBTOption[T] extends DataSlotOption[T] {
  implicit def nbtType: Type[T]
  override def save(t: NBTTagCompound, kind: UpdateKind.Value) =
    value.foreach(v => t.set(name, v))
  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value) =
    t.get[T](name)
}

