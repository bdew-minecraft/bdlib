package net.bdew.lib.multiblock.interact

import net.minecraftforge.energy.IEnergyStorage

trait CIPowerOutput extends CIOutputFaces {
  def powerOutput: IEnergyStorage
}
