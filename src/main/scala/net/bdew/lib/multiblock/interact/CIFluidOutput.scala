package net.bdew.lib.multiblock.interact

import net.minecraftforge.fluids.capability.IFluidHandler

trait CIFluidOutput extends CIOutputFaces {
  def fluidOutput: IFluidHandler
}
