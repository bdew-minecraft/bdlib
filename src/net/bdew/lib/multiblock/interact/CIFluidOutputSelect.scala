/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.data.SlotSet
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo}

trait CIFluidOutputSelect extends CIOutputFaces {
  val outputSlotsDef: SlotSet
  def outputFluid(slot: outputSlotsDef.Slot, resource: FluidStack, doDrain: Boolean): FluidStack
  def outputFluid(slot: outputSlotsDef.Slot, amount: Int, doDrain: Boolean): FluidStack
  def canOutputFluid(slot: outputSlotsDef.Slot, fluid: Fluid): Boolean
  def getTankInfo: Array[FluidTankInfo]
}

