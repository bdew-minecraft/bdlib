package net.bdew.lib.container

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.{ClickType, Slot}
import net.minecraft.world.item.ItemStack

trait SlotClickable extends Slot {
  def onClick(clickType: ClickType, button: Int, player: Player): ItemStack
}
