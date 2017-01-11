/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.gui.SlotClickable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ClickType, IInventory, Slot}
import net.minecraft.item.ItemStack

class SlotSensorParameter[T, R](inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotSensor[T, R], obj: => Option[T]) extends Slot(inv, index, x, y) with SlotClickable {
  def onClick(clickType: ClickType, button: Int, player: EntityPlayer): ItemStack = {
    obj foreach { x =>
      val newParam = ds.sensor.paramClicked(ds.param, player.inventory.getItemStack, clickType, button, x)
      ds := ds.value.copy(param = newParam)
    }
    player.inventory.getItemStack
  }
}
