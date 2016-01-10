/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.resource

import net.bdew.lib.Misc
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

case class ResourceInventoryOutput(res: DataSlotResource) extends IInventory {
  override def getSizeInventory = 1
  override def getInventoryStackLimit = 64

  def getResource =
    res.resource match {
      case Some(Resource(kind: ItemResource, amount)) => Some((kind, amount))
      case _ => None
    }

  override def getStackInSlot(slot: Int) =
    if (slot == 0) {
      (getResource map { case (kind, amount) =>
        val amt = Misc.clamp(amount.floor.toInt, 0, kind.makeStack(1).getMaxStackSize)
        if (amt > 0)
          kind.makeStack(amt)
        else
          null
      }).orNull
    } else null

  override def isItemValidForSlot(slot: Int, stack: ItemStack) = slot == 0

  override def decrStackSize(slot: Int, amt: Int) = {
    if (slot == 0) {
      (for {
        (kind, stored) <- getResource
        drained <- res.rawDrain(amt, true, false)
      } yield {
        val stackSize = Misc.clamp(drained.amount.floor.toInt, 0, kind.makeStack(1).getMaxStackSize)
        if (stackSize > 0) {
          res.rawDrain(stackSize, true, true)
          kind.makeStack(stackSize)
        } else null
      }).orNull
    } else null
  }

  override def setInventorySlotContents(slot: Int, stack: ItemStack) = {
    if (slot == 0) {
      if (stack == null) {
        decrStackSize(0, Int.MaxValue)
      } else {
        val stackRes = Resource.from(stack)
        for ((kind, stored) <- getResource if kind == stackRes.kind && stackRes.amount < stored) {
          res.rawDrain(stored - stackRes.amount, true, true)
        }
      }
    }
  }

  override def removeStackFromSlot(slot: Int) = {
    val stack = getStackInSlot(slot)
    setInventorySlotContents(slot, null)
    stack
  }

  override def clear() = {
    res.resource = None
    markDirty()
  }

  override def closeInventory(player: EntityPlayer) = {}
  override def openInventory(player: EntityPlayer) = {}
  override def isUseableByPlayer(player: EntityPlayer) = true

  override def markDirty() = res.parent.dataSlotChanged(res)

  override def getFieldCount = 0
  override def getField(id: Int) = 0
  override def setField(id: Int, value: Int) = {}
  override def hasCustomName = false
  override def getDisplayName = null
  override def getName = ""
}
