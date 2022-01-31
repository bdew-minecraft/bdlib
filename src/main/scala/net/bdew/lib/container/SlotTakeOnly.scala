package net.bdew.lib.container

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class SlotTakeOnly(inv: Container, slot: Int, x: Int, y: Int) extends BaseSlot(inv, slot, x, y) {
  override def mayPlace(stack: ItemStack): Boolean = false
}