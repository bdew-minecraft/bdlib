package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.energy.IEnergyStorage

class EnergyFillMonitor(val base: IEnergyStorage, onFill: Int => Unit) extends EnergyHandlerProxy {
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    val filled = super.receiveEnergy(maxReceive, simulate)
    if (!simulate && filled > 0) onFill(filled)
    filled
  }
}
