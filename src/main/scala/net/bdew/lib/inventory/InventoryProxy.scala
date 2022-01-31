package net.bdew.lib.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.{Item, ItemStack}

import java.util

class InventoryProxy(inv: Container) extends Container {
  override def getContainerSize: Int = inv.getContainerSize
  override def getMaxStackSize: Int = inv.getMaxStackSize
  override def canPlaceItem(slot: Int, stack: ItemStack): Boolean = inv.canPlaceItem(slot, stack)
  override def getItem(slot: Int): ItemStack = inv.getItem(slot)
  override def setItem(slot: Int, stack: ItemStack): Unit = inv.setItem(slot, stack)
  override def removeItem(slot: Int, count: Int): ItemStack = inv.removeItem(slot, count)
  override def removeItemNoUpdate(slot: Int): ItemStack = inv.removeItemNoUpdate(slot)
  override def isEmpty: Boolean = inv.isEmpty
  override def clearContent(): Unit = inv.clearContent()
  override def setChanged(): Unit = inv.setChanged()
  override def countItem(item: Item): Int = inv.countItem(item)
  override def hasAnyOf(items: util.Set[Item]): Boolean = inv.hasAnyOf(items)
  override def startOpen(player: Player): Unit = inv.startOpen(player)
  override def stopOpen(player: Player): Unit = inv.stopOpen(player)
  override def stillValid(player: Player): Boolean = inv.stillValid(player)
}
