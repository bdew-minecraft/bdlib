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
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack

object TFluidStack extends ConvertedType[FluidStack, NBTTagCompound] {
  override def encode(v: FluidStack) = v.writeToNBT(new NBTTagCompound)
  override def decode(v: NBTTagCompound) = Option(FluidStack.loadFluidStackFromNBT(v))
}
