package net.bdew.lib.nbt.converters

import net.bdew.lib.nbt.ConvertedType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.registries.ForgeRegistries

object TFluid extends ConvertedType[Fluid, String] {
  override def encode(v: Fluid): String = ForgeRegistries.FLUIDS.getKey(v).toString
  override def decode(v: String): Option[Fluid] =
    Option(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(v)))
}
