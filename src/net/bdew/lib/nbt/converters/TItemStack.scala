/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object TItemStack extends ConvertedType[ItemStack, NBTTagCompound] {
  override def encode(v: ItemStack) = v.writeToNBT(new NBTTagCompound)
  override def decode(v: NBTTagCompound) = Some(new ItemStack(v))
}
