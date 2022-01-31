package net.bdew.lib.container

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class SlotValidating(inv: Container, slot: Int, x: Int, y: Int) extends BaseSlot(inv, slot, x, y) {
  override def mayPlace(stack: ItemStack): Boolean = inv.canPlaceItem(slot, stack)
}