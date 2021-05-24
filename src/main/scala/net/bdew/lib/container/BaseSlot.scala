package net.bdew.lib.container

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Slot

class BaseSlot(inv: IInventory, slot: Int, x: Int, y: Int) extends Slot(inv, slot, x, y) {
}
