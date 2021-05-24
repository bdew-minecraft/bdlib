/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.energy.IEnergyStorage

class EnergyDrainMonitor(val base: IEnergyStorage, onDrain: Int => Unit) extends EnergyHandlerProxy {
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    val extracted = super.extractEnergy(maxExtract, simulate)
    if (!simulate && extracted > 0) onDrain(extracted)
    extracted
  }
}
