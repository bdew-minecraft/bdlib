package net.bdew.lib.container.switchable

import net.bdew.lib.container.SlotValidating
import net.minecraft.world.Container

class SwitchableSlot(inv: Container, slot: Int, x: Int, y: Int, isVisible: () => Boolean) extends SlotValidating(inv, slot, x, y) {
  override def isActive: Boolean = isVisible()
}
