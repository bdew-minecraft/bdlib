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
import net.minecraft.nbt.CompoundNBT

object TItemStack extends ConvertedType[ItemStack, CompoundNBT] {
  override def encode(v: ItemStack): CompoundNBT = v.serializeNBT()
  override def decode(v: CompoundNBT): Option[ItemStack] = Some(ItemStack.of(v))
}
