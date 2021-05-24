/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.container

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.{ClickType, Slot}
import net.minecraft.item.ItemStack

trait SlotClickable extends Slot {
  def onClick(clickType: ClickType, button: Int, player: PlayerEntity): ItemStack
}
