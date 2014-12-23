/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

trait ItemCover extends Item {
  /**
   * @return Icon to render, MUST be on terrain sprite sheet (0), not item!
   */
  def getCoverIcon(stack: ItemStack): IIcon

  /**
   * Perform tick, called on server only
   */
  def tickCover(te: TileCoverable, side: ForgeDirection, cover: ItemStack)

  /**
   * Checks if this cover can be installed on a specific TE
   */
  def isValidTile(te: TileCoverable, cover: ItemStack): Boolean
}
