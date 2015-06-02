/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.Misc
import net.bdew.lib.gui.SlotClickable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, Slot}

class SlotSensorType[T, R](inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotSensor[T, R], types: => Seq[GenericSensorType[T, R]]) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(button: Int, mods: Int, player: EntityPlayer) = {
    if (mods == 0 && (button == 0 || button == 1) && types.nonEmpty && player.inventory.getItemStack == null) {
      val newSensor =
        if (button == 0)
          Misc.nextInSeq(types, ds.sensor)
        else
          Misc.prevInSeq(types, ds.sensor)
      ds := SensorPair(newSensor, newSensor.defaultParameter)
    }
    player.inventory.getItemStack
  }
}
