package net.bdew.lib.multiblock.interact

import net.minecraftforge.energy.IEnergyStorage

trait CIPowerInput extends CIOutputFaces {
  def powerInput: IEnergyStorage
}
