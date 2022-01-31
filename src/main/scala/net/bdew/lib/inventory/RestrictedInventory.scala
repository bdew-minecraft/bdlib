package net.bdew.lib.inventory

import net.minecraft.core.Direction
import net.minecraft.world.{Container, WorldlyContainer}
import net.minecraft.world.item.ItemStack

class RestrictedInventory(inv: Container, canExtract: (Int, Direction) => Boolean, canInsert: (Int, ItemStack, Direction) => Boolean) extends InventoryProxy(inv) with WorldlyContainer {
  override def getSlotsForFace(face: Direction): Array[Int] =
    (0 until inv.getContainerSize).toArray

  override def canPlaceItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    canInsert(slot, stack, face) && super.canPlaceItem(slot, stack)

  override def canTakeItemThroughFace(slot: Int, stack: ItemStack, face: Direction): Boolean =
    canExtract(slot, face)

  override def canPlaceItem(slot: Int, stack: ItemStack): Boolean =
    canInsert(slot, stack, null) && super.canPlaceItem(slot, stack)
}
