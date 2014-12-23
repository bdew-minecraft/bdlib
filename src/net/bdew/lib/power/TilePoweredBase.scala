/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import net.bdew.lib.machine.PoweredMachine
import net.bdew.lib.tile.TileExtended

trait TilePoweredBase extends TileExtended {
  def power: DataSlotPower
  def configurePower(cfg: PoweredMachine) = power.configure(cfg)
}
