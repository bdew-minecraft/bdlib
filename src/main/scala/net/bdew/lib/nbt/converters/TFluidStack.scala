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
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fluids.FluidStack

object TFluidStack extends ConvertedType[FluidStack, CompoundNBT] {
  override def encode(v: FluidStack): CompoundNBT = v.writeToNBT(new CompoundNBT)
  override def decode(v: CompoundNBT): Option[FluidStack] = Option(FluidStack.loadFluidStackFromNBT(v))
}
