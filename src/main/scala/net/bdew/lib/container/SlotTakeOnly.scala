package net.bdew.lib.container

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class SlotTakeOnly(inv: IInventory, slot: Int, x: Int, y: Int) extends BaseSlot(inv, slot, x, y) {
  override def mayPlace(stack: ItemStack): Boolean = false
}