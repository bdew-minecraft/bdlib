/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.tile

import net.bdew.lib.misc.RSMode
import net.bdew.lib.multiblock.data.OutputConfigRSControllable
import net.minecraft.tileentity.TileEntity

trait RSControllableOutput extends TileEntity {
  def checkCanOutput(cfg: OutputConfigRSControllable): Boolean = {
    if (cfg.rsMode == RSMode.ALWAYS) return true
    if (cfg.rsMode == RSMode.NEVER) return false
    getLevel.hasNeighborSignal(getBlockPos) ^ (cfg.rsMode == RSMode.RS_OFF)
  }
}
