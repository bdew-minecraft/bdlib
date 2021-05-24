package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.fluid.Fluid
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

object TFluid extends ConvertedType[Fluid, String] {
  override def encode(v: Fluid): String = v.getRegistryName.toString
  override def decode(v: String): Option[Fluid] =
    Option(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(v)))
}
