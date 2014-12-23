/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.minecraftforge.fluids.{Fluid, FluidRegistry}

class FluidManager() {
  def regFluid[T <: Fluid](fluid: T): T = {
    FluidRegistry.registerFluid(fluid)
    return fluid
  }
  def load() {}
}
