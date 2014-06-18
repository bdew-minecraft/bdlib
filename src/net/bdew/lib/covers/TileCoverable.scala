/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.covers

import net.bdew.lib.data.base.{UpdateKind, TileDataSlots}
import net.bdew.lib.data.DataSlotItemStack
import net.bdew.lib.Misc
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection

trait TileCoverable extends TileDataSlots {
  val covers = (ForgeDirection.VALID_DIRECTIONS map { x =>
    x -> DataSlotItemStack("cover_" + x.toString.toLowerCase, this).setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER)
  }).toMap

  /**
   * Checks if a specific cover can be installed here
   */
  def isValidCover(side: ForgeDirection, cover: ItemStack): Boolean

  /**
   * Called when new covers are installed
   */
  def onCoversChanged() {}

  def tickCovers() =
    for {
      (dir, coverSlot) <- covers
      coverStack <- Option(coverSlot.cval)
      coverItem <- Option(coverStack.getItem) flatMap (Misc.asInstanceOpt(_, classOf[ItemCover]))
    } {
      coverItem.tickCover(this, dir, coverStack)
    }

  serverTick.listen(tickCovers)
}
