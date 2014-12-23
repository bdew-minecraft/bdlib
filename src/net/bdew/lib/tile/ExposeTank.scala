/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

trait ExposeTank extends TileEntity with IFluidHandler {
  def getTankFromDirection(dir: ForgeDirection): IFluidTank

  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
    val tank = getTankFromDirection(from)
    if (tank == null) return 0
    return tank.fill(resource, doFill)
  }

  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    val tank = getTankFromDirection(from)
    if (tank == null || resource == null || !resource.isFluidEqual(tank.getFluid)) return null
    return tank.drain(resource.amount, doDrain)
  }

  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = {
    val tank = getTankFromDirection(from)
    if (tank == null) return null
    getTankFromDirection(from).drain(maxDrain, doDrain)
  }

  def canFill(from: ForgeDirection, fluid: Fluid): Boolean = getTankFromDirection(from) != null

  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = getTankFromDirection(from) != null

  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = {
    val tank = getTankFromDirection(from)
    if (tank == null) return Array.empty[FluidTankInfo]
    return Array(tank.getInfo)
  }
}
