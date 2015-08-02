/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock

import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.block.BlockController
import net.bdew.lib.recipes.gencfg.{ConfigSection, EntryDouble}

trait MachineCore extends {
  this: Machine[_ <: BlockController[_]] =>
  var tuning: ConfigSection

  def getController: BlockController[_] = block

  def cfgSectionToMap(name: String) = tuning.getSection(name).map({
    case (n, EntryDouble(v)) => n -> v.toInt
    case (n, x) => sys.error("Invalid config value in section %s%s.%s = %s".format(tuning.pfx, name, n, x))
  }).toMap

  lazy val modules = cfgSectionToMap("Modules")
  lazy val required = cfgSectionToMap("Required")
}
