package net.bdew.lib.capabilities.handlers

import net.bdew.lib.power.DataSlotPower
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage

class PowerEnergyHandler(power: DataSlotPower, receive: Boolean, extract: Boolean) extends IEnergyStorage {
  override def getEnergyStored: Int = power.stored.floor.toInt
  override def getMaxEnergyStored: Int = power.capacity.floor.toInt

  override def canReceive: Boolean = receive
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    if (receive)
      power.inject(maxReceive.toFloat, simulate).floor.toInt
    else
      0
  }

  override def canExtract: Boolean = extract
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int =
    if (extract)
      power.extract(maxExtract.toFloat, simulate).floor.toInt
    else
      0
}

object PowerEnergyHandler {
  def create(power: DataSlotPower, receive: Boolean, extract: Boolean): LazyOptional[IEnergyStorage] = {
    val handler = new PowerEnergyHandler(power, receive, extract)
    LazyOptional.of(() => handler)
  }
}
