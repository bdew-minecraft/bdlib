package net.bdew.lib.capabilities.handlers

import net.minecraft.world.WorldlyContainer
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.SidedInvWrapper

object SidedInventoryItemHandler {
  def create(inv: WorldlyContainer): LazyOptional[IItemHandler] = {
    val handler = new SidedInvWrapper(inv, null)
    LazyOptional.of(() => handler)
  }
}