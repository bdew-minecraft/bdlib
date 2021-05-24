package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.energy.IEnergyStorage

object EnergyHandlerNull extends IEnergyStorage {
  override def getEnergyStored: Int = 0
  override def getMaxEnergyStored: Int = 0
  override def canExtract: Boolean = false
  override def canReceive: Boolean = false
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = 0
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
}
