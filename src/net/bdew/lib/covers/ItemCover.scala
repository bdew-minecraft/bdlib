/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.bdew.lib.items.BaseItem
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

trait ItemCover extends BaseItem {
  /**
    * @return true if tickCover should be called
    */
  def isCoverTicking: Boolean

  /**
    * Perform tick, called on server only
    *
    * @param te    Host TileEntity
    * @param side  Side the cover is installed on
    * @param cover Cover ItemStack
    */
  def tickCover(te: TileCoverable, side: EnumFacing, cover: ItemStack): Unit = {}

  /**
    * Checks if this cover can be installed on a specific TE on a specific side
    *
    * @param te    Potential host TileEntity
    * @param cover Cover ItemStack
    * @param side  Side that the cover is going to be on
    * @return true if can be installed
    */
  def isValidTile(te: TileCoverable, side: EnumFacing, cover: ItemStack): Boolean

  /**
    * Handle click on covered side - called on both client and server
    *
    * @param te     Host TileEntity
    * @param side   Side the cover is installed on
    * @param cover  Cover ItemStack
    * @param player Player clicking the cover
    * @return true if click was handled by cover
    */
  def clickCover(te: TileCoverable, side: EnumFacing, cover: ItemStack, player: EntityPlayer): Boolean = false

  /**
    * Check if cover is emitting RS signal
    *
    * @param te    Host TileEntity
    * @param side  Side the cover is installed on
    * @param cover Cover ItemStack
    * @return true if cover is emitting RS signal
    */
  def isEmittingSignal(te: TileCoverable, side: EnumFacing, cover: ItemStack): Boolean = false

  /**
    * Called when cover is installed on a block - called only on server
    *
    * @param te     Host TileEntity
    * @param side   Side the cover is installed on
    * @param cover  Cover ItemStack - do not modify!
    * @param player Player entity doing the installation
    * @return cover ItemStack - copy if modified
    */
  def onInstall(te: TileCoverable, side: EnumFacing, cover: ItemStack, player: EntityPlayerMP): ItemStack = cover

  /**
    * Called when cover is removed from block or block is destroyed - called only on server
    *
    * @param cover Cover ItemStack - do not modify!
    * @return cover ItemStack - copy if modified
    */
  def onRemove(cover: ItemStack): ItemStack = cover

  /**
    * Called when rendering cover, override to change what item is used for display
    *
    * @param te    Host TileEntity
    * @param side  Side the cover is installed on
    * @param cover Cover ItemStack - do not modify!
    * @return
    */
  def getDisplayItem(te: TileCoverable, side: EnumFacing, cover: ItemStack): ItemStack = cover
}
