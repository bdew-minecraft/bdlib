/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

trait InventoryProxy extends TileEntity with IInventory {
  def targetInventory: Option[IInventory]

  override def openInventory() = targetInventory foreach (_.openInventory())
  override def closeInventory() = targetInventory foreach (_.closeInventory())

  override def markDirty() = {
    targetInventory foreach (_.markDirty())
    super.markDirty()
  }

  override def getSizeInventory = targetInventory map (_.getSizeInventory) getOrElse 0
  override def getInventoryStackLimit = targetInventory map (_.getInventoryStackLimit) getOrElse 64

  override def getStackInSlot(slot: Int) = targetInventory.map(_.getStackInSlot(slot)).orNull
  override def getStackInSlotOnClosing(slot: Int) = targetInventory.map(_.getStackInSlotOnClosing(slot)).orNull
  override def setInventorySlotContents(slot: Int, item: ItemStack) = targetInventory.foreach(_.setInventorySlotContents(slot, item))
  override def decrStackSize(slot: Int, num: Int) = targetInventory.map(_.decrStackSize(slot, num)).orNull

  override def isItemValidForSlot(slot: Int, item: ItemStack) = targetInventory.exists(_.isItemValidForSlot(slot, item))
  override def isUseableByPlayer(player: EntityPlayer) = targetInventory.exists(_.isUseableByPlayer(player))

  override def getInventoryName = targetInventory.map(_.getInventoryName).getOrElse("")
  override def hasCustomInventoryName = targetInventory.exists(_.hasCustomInventoryName)
}
