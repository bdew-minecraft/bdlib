/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.config

import net.bdew.lib.Misc
import net.bdew.lib.gui.{GuiHandler, GuiProvider}
import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.MachineCore
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraft.creativetab.CreativeTabs

class MachineManager(val tuning: ConfigSection, guiHandler: GuiHandler, creativeTab: CreativeTabs) {
  def registerMachine[R <: Machine[_]](machine: R): R = {
    machine.tuning = tuning.getSection(machine.name)
    if (machine.tuning.getBoolean("Enabled")) {
      machine.enabled = true
      machine.regBlock(creativeTab)
      if (machine.isInstanceOf[GuiProvider])
        guiHandler.register(machine.asInstanceOf[GuiProvider])
    }
    return machine
  }

  def load() {}
}

trait MachineManagerMultiblock extends MachineManager {
  var controllers = Set.empty[MachineCore]

  override def registerMachine[R <: Machine[_]](machine: R): R = {
    super.registerMachine(machine)
    if (machine.enabled) {
      Misc.asInstanceOpt(machine, classOf[MachineCore]) foreach { controllerMachine =>
        controllers += controllerMachine
        controllerMachine.getController.machine = controllerMachine
      }
    }
    machine
  }

  def getMachinesForBlock(b: BlockModule[_]): Map[MachineCore, (Int, Int)] = {
    (for (machine <- controllers; max <- machine.modules.get(b.kind)) yield {
      machine ->(machine.required.getOrElse(b.kind, 0), max)
    }).toMap
  }
}