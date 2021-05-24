package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

object TItemStack extends ConvertedType[ItemStack, CompoundNBT] {
  override def encode(v: ItemStack): CompoundNBT = v.serializeNBT()
  override def decode(v: CompoundNBT): Option[ItemStack] = Some(ItemStack.of(v))
}
