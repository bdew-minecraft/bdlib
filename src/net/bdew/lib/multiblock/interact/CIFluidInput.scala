/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.tile.TileController
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo}

trait CIFluidInput extends TileController {
  def inputFluid(resource: FluidStack, doFill: Boolean): Int
  def canInputFluid(fluid: Fluid): Boolean
  def getTankInfo: Array[FluidTankInfo]
}
