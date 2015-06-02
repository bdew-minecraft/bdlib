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

class MultipleInventoryAdapter(val map: Map[Int, (IInventory, Int)]) extends IInventory {
  def this(inventories: Iterable[IInventory]) =
    this((inventories flatMap { inv => (0 until inv.getSizeInventory).map((inv, _)) }).zipWithIndex.map(_.swap).toMap)
  def this(inventories: IInventory*) =
    this((inventories flatMap { inv => (0 until inv.getSizeInventory).map((inv, _)) }).zipWithIndex.map(_.swap).toMap)

  lazy val inventories = map.values.map(_._1).toSet
  override lazy val getInventoryStackLimit = inventories.map(_.getInventoryStackLimit).min
  override lazy val getSizeInventory = map.keySet.max + 1

  private def run[T](slot: Int, f: (IInventory, Int) => T): Option[T] =
    map.get(slot).map(f.tupled)

  override def decrStackSize(slot: Int, num: Int) =
    run(slot, (i, s) => i.decrStackSize(s, num)).orNull

  override def isItemValidForSlot(slot: Int, item: ItemStack) =
    run(slot, (i, s) => i.isItemValidForSlot(s, item)).getOrElse(false)

  override def getStackInSlotOnClosing(slot: Int) =
    run(slot, (i, s) => i.getStackInSlotOnClosing(s)).orNull

  override def setInventorySlotContents(slot: Int, stack: ItemStack) =
    run(slot, (i, s) => i.setInventorySlotContents(s, stack))

  override def getStackInSlot(slot: Int) =
    run(slot, (i, s) => i.getStackInSlot(s)).orNull

  override def markDirty() = inventories.foreach(_.markDirty())
  override def closeInventory() = inventories.foreach(_.closeInventory())
  override def openInventory() = inventories.foreach(_.openInventory())
  override def isUseableByPlayer(p: EntityPlayer) = inventories.exists(_.isUseableByPlayer(p))

  override def hasCustomInventoryName = false
  override def getInventoryName = ""
}
