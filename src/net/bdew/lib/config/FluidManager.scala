/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.config

import net.minecraftforge.fluids.{FluidRegistry, Fluid}

class FluidManager() {
  def regFluid[T <: Fluid](fluid: T): T = {
    FluidRegistry.registerFluid(fluid)
    return fluid
  }
  def load() {}
}
