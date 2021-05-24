package net.bdew.lib.inventory

import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction

class SidedInventoryProxy(inv: ISidedInventory) extends InventoryProxy(inv) with ISidedInventory {
  override def getSlotsForFace(face: Direction): Array[Int] = inv.getSlotsForFace(face)
  override def canPlaceItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    inv.canPlaceItemThroughFace(slot, stack, face)
  override def canTakeItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    inv.canTakeItemThroughFace(slot, stack, face)
}
