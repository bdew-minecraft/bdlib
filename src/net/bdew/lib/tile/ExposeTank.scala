/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids._

trait ExposeTank extends TileEntity with IFluidHandler {
  def getTankFromDirection(dir: EnumFacing): IFluidTank

  def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean): Int = {
    val tank = getTankFromDirection(from)
    if (tank == null) return 0
    return tank.fill(resource, doFill)
  }

  def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack = {
    val tank = getTankFromDirection(from)
    if (tank == null || resource == null || !resource.isFluidEqual(tank.getFluid)) return null
    return tank.drain(resource.amount, doDrain)
  }

  def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean): FluidStack = {
    val tank = getTankFromDirection(from)
    if (tank == null) return null
    getTankFromDirection(from).drain(maxDrain, doDrain)
  }

  def canFill(from: EnumFacing, fluid: Fluid): Boolean = getTankFromDirection(from) != null

  def canDrain(from: EnumFacing, fluid: Fluid): Boolean = getTankFromDirection(from) != null

  def getTankInfo(from: EnumFacing): Array[FluidTankInfo] = {
    val tank = getTankFromDirection(from)
    if (tank == null) return Array.empty[FluidTankInfo]
    return Array(tank.getInfo)
  }
}
