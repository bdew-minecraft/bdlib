/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.adapters

import net.bdew.lib.capabilities.CapAdapter
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidTankProperties}
import net.minecraftforge.fluids.{FluidStack, FluidTankInfo, IFluidHandler => OldFluidHandler}

object OldFluidHandlerAdapter extends CapAdapter[IFluidHandler] {
  override def canWrap(tile: TileEntity, side: EnumFacing): Boolean = tile.isInstanceOf[OldFluidHandler]
  override def wrap(tile: TileEntity, side: EnumFacing): Option[IFluidHandler] =
    if (tile.isInstanceOf[OldFluidHandler])
      Some(new HandlerWrapper(tile.asInstanceOf[OldFluidHandler], side))
    else
      None

  class HandlerWrapper(base: OldFluidHandler, side: EnumFacing) extends IFluidHandler {

    private class PropWrapper(data: FluidTankInfo) extends IFluidTankProperties {
      override def canDrainFluidType(fluidStack: FluidStack): Boolean = base.canDrain(side, fluidStack.getFluid)
      override def canFillFluidType(fluidStack: FluidStack): Boolean = base.canFill(side, fluidStack.getFluid)
      override def canFill: Boolean = true
      override def canDrain: Boolean = true
      override def getContents: FluidStack = data.fluid
      override def getCapacity: Int = data.capacity
    }

    override def getTankProperties: Array[IFluidTankProperties] = base.getTankInfo(side) map (new PropWrapper(_))

    override def drain(resource: FluidStack, doDrain: Boolean): FluidStack = base.drain(side, resource, doDrain)
    override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = base.drain(side, maxDrain, doDrain)

    override def fill(resource: FluidStack, doFill: Boolean): Int = base.fill(side, resource, doFill)
  }

}