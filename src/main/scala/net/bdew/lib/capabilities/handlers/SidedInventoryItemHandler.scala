package net.bdew.lib.capabilities.handlers

import net.minecraft.inventory.ISidedInventory
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.SidedInvWrapper

object SidedInventoryItemHandler {
  def create(inv: ISidedInventory): LazyOptional[IItemHandler] = {
    val handler = new SidedInvWrapper(inv, null)
    LazyOptional.of(() => handler)
  }
}