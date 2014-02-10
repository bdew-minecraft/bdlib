/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.tile.inventory

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer

trait BaseInventory extends IInventory {
  var inv: Array[ItemStack] = new Array(getSizeInventory)

  def getStackInSlot(i: Int): ItemStack = inv(i)

  def getInvName = ""

  def isInvNameLocalized = false

  def getInventoryStackLimit = 64

  def isUseableByPlayer(player: EntityPlayer) = true

  def openChest() = Unit

  def closeChest() = Unit

  def isItemValidForSlot(slot: Int, stack: ItemStack) = true

  def decrStackSize(slot: Int, n: Int): ItemStack = {
    var item = inv(slot)
    if (item != null) {
      if (item.stackSize <= n) {
        inv(slot) = null
        markDirty()
        return item
      } else {
        var newstack = item.splitStack(n)
        if (item.stackSize == 0) {
          inv(slot) = null
        }
        markDirty()
        return newstack
      }
    } else {
      return null
    }
  }

  def getStackInSlotOnClosing(slot: Int): ItemStack = {
    var tmp = inv(slot)
    inv(slot) = null
    markDirty()
    return tmp
  }

  def setInventorySlotContents(slot: Int, stack: ItemStack) = {
    inv(slot) = stack
    markDirty()
  }
}
