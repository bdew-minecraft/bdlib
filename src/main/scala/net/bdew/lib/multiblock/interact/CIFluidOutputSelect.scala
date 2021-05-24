package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.data.SlotSet
import net.minecraftforge.fluids.capability.IFluidHandler

trait CIFluidOutputSelect extends CIOutputFaces {
  val outputSlotsDef: SlotSet
  def fluidOutputForSlot(slot: outputSlotsDef.Slot): IFluidHandler
}

