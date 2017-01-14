/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.bdew.lib.capabilities.{Capabilities, CapabilityProvider}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.wrapper.SidedInvWrapper

trait InventoryProxy extends TileEntity with CapabilityProvider with ISidedInventory {
  def targetInventory: Option[IInventory]

  addCachedSidedCapability(Capabilities.CAP_ITEM_HANDLER, new SidedInvWrapper(this, _))

  override def openInventory(player: EntityPlayer) = targetInventory foreach (_.openInventory(player))
  override def closeInventory(player: EntityPlayer) = targetInventory foreach (_.closeInventory(player))

  override def markDirty() = {
    targetInventory foreach (_.markDirty())
    super.markDirty()
  }

  override def getSizeInventory = targetInventory map (_.getSizeInventory) getOrElse 0
  override def getInventoryStackLimit = targetInventory map (_.getInventoryStackLimit) getOrElse 64

  override def getStackInSlot(slot: Int) = targetInventory.map(_.getStackInSlot(slot)).getOrElse(ItemStack.EMPTY)
  override def setInventorySlotContents(slot: Int, item: ItemStack) = targetInventory.foreach(_.setInventorySlotContents(slot, item))
  override def decrStackSize(slot: Int, num: Int) = targetInventory.map(_.decrStackSize(slot, num)).getOrElse(ItemStack.EMPTY)
  override def removeStackFromSlot(slot: Int) = targetInventory.map(_.removeStackFromSlot(slot)).getOrElse(ItemStack.EMPTY)

  override def isItemValidForSlot(slot: Int, item: ItemStack) = targetInventory.exists(_.isItemValidForSlot(slot, item))
  override def isUsableByPlayer(player: EntityPlayer) = targetInventory.exists(_.isUsableByPlayer(player))

  override def clear() = targetInventory foreach (_.clear())
  override def isEmpty: Boolean = targetInventory exists (_.isEmpty)

  override def getFieldCount = targetInventory map (_.getFieldCount()) getOrElse 0
  override def getField(id: Int) = targetInventory map (_.getField(id)) getOrElse 0
  override def setField(id: Int, value: Int) = targetInventory foreach (_.setField(id, value))

  override def getDisplayName = (targetInventory map (_.getDisplayName)).orNull
  override def getName = (targetInventory map (_.getName)).orNull
  override def hasCustomName = targetInventory exists (_.hasCustomName)
}
