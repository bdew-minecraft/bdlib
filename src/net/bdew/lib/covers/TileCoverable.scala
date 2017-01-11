/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import java.util.Locale

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.DataSlotOption
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.tile.TileTicking
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

trait TileCoverable extends TileDataSlots with TileTicking {
  val covers = (EnumFacing.values() map { x =>
    x -> DataSlotOption[ItemStack]("cover_" + x.toString.toLowerCase(Locale.US), this).setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER)
  }).toMap

  /**
    * Checks if a specific cover can be installed here
    */
  def isValidCover(side: EnumFacing, cover: ItemStack): Boolean

  /**
    * Called when new covers are installed
    */
  def onCoversChanged() {}

  def tickCovers() =
    for {
      (dir, coverSlot) <- covers
      coverStack <- coverSlot
      coverItem <- Option(coverStack.getItem) flatMap (Misc.asInstanceOpt(_, classOf[ItemCover]))
    } {
      coverItem.tickCover(this, dir, coverStack)
    }

  serverTick.listen(tickCovers)
}
