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

trait BaseInventory extends IInventory {
  var inv: Array[ItemStack] = new Array(getSizeInventory)

  def getStackInSlot(i: Int): ItemStack = inv(i)

  def getInventoryName = ""

  def hasCustomInventoryName = false

  def getInventoryStackLimit = 64

  def isUseableByPlayer(player: EntityPlayer) = true

  def openInventory() {}

  def closeInventory() {}

  def isItemValidForSlot(slot: Int, stack: ItemStack) = true

  def decrStackSize(slot: Int, n: Int): ItemStack = {
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
