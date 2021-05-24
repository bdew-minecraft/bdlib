package net.bdew.lib.capabilities.helpers

import net.minecraftforge.energy.IEnergyStorage

object EnergyHelper {
  /**
   * Attempt to move energy between 2 handlers
   *
   * @param from     source handler
   * @param to       destination handler
   * @param simulate if true transfer will be just simulated
   * @param max      max amount of fluid to move
   * @return amount of energy moved
   */
  def pushEnergy(from: IEnergyStorage, to: IEnergyStorage, simulate: Boolean, max: Int = Integer.MAX_VALUE): Int = {
    val drainSim = from.extractEnergy(max, true)
    if (drainSim > 0) {
      val fillSim = to.receiveEnergy(drainSim, true)
      if (fillSim > 0) {
        val drainSim2 = from.extractEnergy(fillSim, true)
        if (drainSim2 > 0) {
          val fillReal = to.receiveEnergy(drainSim2, simulate)
          if (fillReal > 0) {
            if (!simulate)
              from.extractEnergy(fillReal, false)
            return fillReal
          }
        }
      }
    }
    0
  }
}
