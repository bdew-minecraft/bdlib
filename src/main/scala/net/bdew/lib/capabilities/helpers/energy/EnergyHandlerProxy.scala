package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.energy.IEnergyStorage

trait EnergyHandlerProxy extends IEnergyStorage {
  def base: IEnergyStorage
  override def getEnergyStored: Int = base.getEnergyStored
  override def getMaxEnergyStored: Int = base.getMaxEnergyStored
  override def canExtract: Boolean = base.canExtract
  override def canReceive: Boolean = base.canReceive
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = base.receiveEnergy(maxReceive, simulate)
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = base.extractEnergy(maxExtract, simulate)
}
