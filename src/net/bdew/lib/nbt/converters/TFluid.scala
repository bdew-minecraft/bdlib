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
import net.minecraftforge.fluids.{Fluid, FluidRegistry}

object TFluid extends ConvertedType[Fluid, String] {
  override def encode(v: Fluid) = v.getName
  override def decode(v: String) =
    if (FluidRegistry.isFluidRegistered(v))
      Option(FluidRegistry.getFluid(v))
    else
      None
}
