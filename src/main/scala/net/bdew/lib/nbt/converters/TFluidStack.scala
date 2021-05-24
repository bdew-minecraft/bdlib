package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fluids.FluidStack

object TFluidStack extends ConvertedType[FluidStack, CompoundNBT] {
  override def encode(v: FluidStack): CompoundNBT = v.writeToNBT(new CompoundNBT)
  override def decode(v: CompoundNBT): Option[FluidStack] = Option(FluidStack.loadFluidStackFromNBT(v))
}
