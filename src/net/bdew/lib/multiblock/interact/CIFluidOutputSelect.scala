/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.multiblock.data.SlotSet
import net.minecraftforge.fluids.capability.IFluidHandler

trait CIFluidOutputSelect extends CIOutputFaces {
  val outputSlotsDef: SlotSet
  def getOutputTanksForSlot(slot: outputSlotsDef.Slot): List[IFluidHandler]
}

