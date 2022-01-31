package net.bdew.lib.inventory

import net.minecraft.core.Direction
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.item.ItemStack

class SidedInventoryProxy(inv: WorldlyContainer) extends InventoryProxy(inv) with WorldlyContainer {
  override def getSlotsForFace(face: Direction): Array[Int] = inv.getSlotsForFace(face)
  override def canPlaceItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    inv.canPlaceItemThroughFace(slot, stack, face)
  override def canTakeItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    inv.canTakeItemThroughFace(slot, stack, face)
}
