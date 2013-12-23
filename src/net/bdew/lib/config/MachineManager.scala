/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.config

import net.bdew.lib.recipes.gencfg.ConfigSection
import net.bdew.lib.machine.Machine
import net.bdew.lib.gui.{GuiProvider, GuiHandler}

class MachineManager(ids: IdManager, val tuning: ConfigSection, guiHandler: GuiHandler) {
  def registerMachine[R <: Machine[_]](machine: R): R = {
    machine.tuning = tuning.getSection(machine.name)
    if (machine.tuning.getBoolean("Enabled")) {
      machine.enabled = true
      machine.regBlock(ids)
      if (machine.isInstanceOf[GuiProvider])
        guiHandler.register(machine.asInstanceOf[GuiProvider])
    }
    return machine
  }
  def load() {}
}
