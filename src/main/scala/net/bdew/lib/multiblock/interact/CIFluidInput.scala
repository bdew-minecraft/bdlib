package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.tile.TileController
import net.minecraftforge.fluids.capability.IFluidHandler

trait CIFluidInput extends TileController {
  def fluidInput: IFluidHandler
}
