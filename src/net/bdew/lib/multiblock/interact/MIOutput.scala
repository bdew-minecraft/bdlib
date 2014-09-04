/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.interact

import net.bdew.lib.BdLib
import net.bdew.lib.multiblock.data.OutputConfig
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraftforge.common.util.ForgeDirection

trait MIOutput[T <: OutputConfig] extends TileModule {
  val outputConfigType: Class[T]
  type OCType = T

  def doOutput(face: ForgeDirection, cfg: OCType)

  def makeCfgObject(face: ForgeDirection): OutputConfig
}
