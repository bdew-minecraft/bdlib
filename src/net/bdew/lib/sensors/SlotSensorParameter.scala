/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.gui.SlotClickable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, Slot}

class SlotSensorParameter(inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotSensor) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(button: Int, mods: Int, player: EntityPlayer) = {
    val newParam = ds.sensor.paramClicked(ds.param, player.inventory.getItemStack, button, mods)
    ds := ds.value.copy(param = newParam)
    player.inventory.getItemStack
  }
}
