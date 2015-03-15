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
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

case class DataSlotItemStack(name: String, parent: DataSlotContainer) extends DataSlotVal[ItemStack] {
  var value: ItemStack = null

  override def update(v: ItemStack) = {
    if (!ItemStack.areItemStacksEqual(v, value)) {
      value = if (v == null) null else v.copy()
      parent.dataSlotChanged(this)
    }
  }

  def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    val tag = new NBTTagCompound()
    if (value != null) value.writeToNBT(tag)
    t.setTag(name, tag)
  }
  def load(t: NBTTagCompound, kind: UpdateKind.Value) = value = ItemStack.loadItemStackFromNBT(t.getCompoundTag(name))
}
