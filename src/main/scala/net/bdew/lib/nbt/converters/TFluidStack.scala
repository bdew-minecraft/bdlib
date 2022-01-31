package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.fluids.FluidStack

object TFluidStack extends ConvertedType[FluidStack, CompoundTag] {
  override def encode(v: FluidStack): CompoundTag = v.writeToNBT(new CompoundTag)
  override def decode(v: CompoundTag): Option[FluidStack] = Option(FluidStack.loadFluidStackFromNBT(v))
}
