package net.bdew.lib.container

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.{ClickType, Slot}
import net.minecraft.item.ItemStack

trait SlotClickable extends Slot {
  def onClick(clickType: ClickType, button: Int, player: PlayerEntity): ItemStack
}
