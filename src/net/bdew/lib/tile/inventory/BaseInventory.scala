/*
 * Copyright (c) bdew, 2013 - 2016
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
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.items.wrapper.SidedInvWrapper

trait BaseInventory extends IInventory {
  var inv: Array[ItemStack] = new Array(getSizeInventory)

  def getInventoryStackLimit = 64

  override def hasCustomName = false
  override def getDisplayName: ITextComponent = null
  override def getName = ""

  override def isUseableByPlayer(player: EntityPlayer) = true
  override def closeInventory(player: EntityPlayer) = {}
  override def openInventory(player: EntityPlayer) = {}

  override def getStackInSlot(i: Int): ItemStack = inv(i)

  override def isItemValidForSlot(slot: Int, stack: ItemStack) = true

  override def setInventorySlotContents(slot: Int, stack: ItemStack) = {
    inv(slot) = stack
    markDirty()
  }

  override def removeStackFromSlot(slot: Int) = {
    val st = inv(slot)
    inv(slot) = null
    markDirty()
    st
  }

  override def decrStackSize(slot: Int, n: Int): ItemStack = {
    val item = inv(slot)
    if (item != null) {
      if (item.stackSize <= n) {
        inv(slot) = null
        markDirty()
        return item
      } else {
        val newStack = item.splitStack(n)
        if (item.stackSize == 0) {
          inv(slot) = null
        }
        markDirty()
        return newStack
      }
    } else {
      return null
    }
  }

  override def clear() = {
    inv = new Array(getSizeInventory)
    markDirty()
  }

  override def getFieldCount = 0
  override def getField(id: Int) = 0
  override def setField(id: Int, value: Int) = {}
}

trait InventoryTile extends TileEntity with CapabilityProvider with BaseInventory with ISidedInventory {
  // To make the compiler happy
  override def getDisplayName: ITextComponent = super.getDisplayName

  addCachedSidedCapability(Capabilities.CAP_ITEM_HANDLER, new SidedInvWrapper(this, _))
}