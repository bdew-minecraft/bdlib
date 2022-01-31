package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

object TItemStack extends ConvertedType[ItemStack, CompoundTag] {
  override def encode(v: ItemStack): CompoundTag = v.serializeNBT()
  override def decode(v: CompoundTag): Option[ItemStack] = Some(ItemStack.of(v))
}
