/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.covers

import net.bdew.lib.data.base.TileDataSlots
import net.minecraftforge.common.ForgeDirection

trait TileCoverable extends TileDataSlots {
  val covers = (ForgeDirection.VALID_DIRECTIONS map { x => x -> DataslotCover("cv_" + x.toString.toLowerCase, this) }).toMap

  /**
   * Checks if a specific cover can be installed here
   */
  def isValidCover(side: ForgeDirection, cover: ItemCover): Boolean

  /**
   * Called when new covers are installed
   */
  def onCoversChanged() {}

  def tickCovers() =
    for ((dir, covopt) <- covers; cover <- covopt.cval)
      cover.tickCover(this, dir)

  serverTick.listen(tickCovers)
}
