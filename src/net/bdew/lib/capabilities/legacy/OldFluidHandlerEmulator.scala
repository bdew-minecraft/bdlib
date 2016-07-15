/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.legacy

import net.bdew.lib.capabilities.Capabilities
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo, IFluidHandler => OldFluidHandler}

/**
  * Mix-in that emulates the old style handlers, for use in TEs that implement the new Capability api
  */
trait OldFluidHandlerEmulator extends TileEntity with OldFluidHandler {
  private def getCap(side: EnumFacing): Option[IFluidHandler] =
    if (hasCapability(Capabilities.CAP_FLUID_HANDLER, side))
      Option(getCapability(Capabilities.CAP_FLUID_HANDLER, side))
    else None

  final override def canFill(from: EnumFacing, fluid: Fluid): Boolean =
    getCap(from).exists(_.getTankProperties.exists(_.canFillFluidType(new FluidStack(fluid, 0))))

  final override def canDrain(from: EnumFacing, fluid: Fluid): Boolean =
    getCap(from).exists(_.getTankProperties.exists(_.canDrainFluidType(new FluidStack(fluid, 0))))

  final override def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack =
    getCap(from).map(_.drain(resource, doDrain)).orNull

  final override def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean): FluidStack =
    getCap(from).map(_.drain(maxDrain, doDrain)).orNull

  final override def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean): Int =
    getCap(from).map(_.fill(resource, doFill)).getOrElse(0)

  final override def getTankInfo(from: EnumFacing): Array[FluidTankInfo] =
    getCap(from).map(_.getTankProperties.map(x => new FluidTankInfo(x.getContents, x.getCapacity))).getOrElse(Array.empty)
}
