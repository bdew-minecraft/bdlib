/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

trait ItemCover extends Item {
  /**
   * @return true if tickCover should be called
   */
  def isCoverTicking: Boolean

  /**
   * @param te Host TileEntity
   * @param side Side the cover is installed on
   * @param cover Cover ItemStack
   * @return Icon to render, MUST be on terrain sprite sheet (0), not item!
   */
  def getCoverIcon(te: TileCoverable, side: ForgeDirection, cover: ItemStack): IIcon

  /**
   * Perform tick, called on server only
   * @param te Host TileEntity
   * @param side Side the cover is installed on
   * @param cover Cover ItemStack
   */
  def tickCover(te: TileCoverable, side: ForgeDirection, cover: ItemStack): Unit = {}

  /**
   * Checks if this cover can be installed on a specific TE
   * @param te Potential host TileEntity
   * @param cover Cover ItemStack
   * @return true if can be installed
   */
  def isValidTile(te: TileCoverable, cover: ItemStack): Boolean

  /**
   * Handle click on covered side - called on both client and server
   * @param te Host TileEntity
   * @param side Side the cover is installed on
   * @param cover Cover ItemStack
   * @param player Player clicking the cover
   * @return true if click was handled by cover
   */
  def clickCover(te: TileCoverable, side: ForgeDirection, cover: ItemStack, player: EntityPlayer): Boolean = false

  /**
   * Check if cover is emitting RS signal
   * @param te Host TileEntity
   * @param side Side the cover is installed on
   * @param cover Cover ItemStack
   * @return true if cover is emitting RS signal
   */
  def isEmittingSignal(te: TileCoverable, side: ForgeDirection, cover: ItemStack): Boolean = false

  /**
   * Called when cover is installed on a block - called only on server
   * @param te Host TileEntity
   * @param side Side the cover is installed on
   * @param cover Cover ItemStack - do not modify!
   * @param player Player entity doing the installation
   * @return cover ItemStack - copy if modified
   */
  def onInstall(te:TileCoverable, side: ForgeDirection, cover: ItemStack, player: EntityPlayerMP): ItemStack = cover

  /**
   * Called when cover is removed from block or block is destroyed - called only on server
   * @param cover Cover ItemStack - do not modify!
   * @return cover ItemStack - copy if modified
   */
  def onRemove(cover: ItemStack): ItemStack = cover
}
