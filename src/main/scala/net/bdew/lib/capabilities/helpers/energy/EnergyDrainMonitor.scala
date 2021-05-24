package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.energy.IEnergyStorage

class EnergyDrainMonitor(val base: IEnergyStorage, onDrain: Int => Unit) extends EnergyHandlerProxy {
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    val extracted = super.extractEnergy(maxExtract, simulate)
    if (!simulate && extracted > 0) onDrain(extracted)
    extracted
  }
}
