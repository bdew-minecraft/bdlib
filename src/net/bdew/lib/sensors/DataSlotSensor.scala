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
import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.bdew.lib.sensors.multiblock.TileSensorModule
import net.minecraft.nbt.NBTTagCompound

case class DataSlotSensor(name: String, parent: TileSensorModule, default: SensorType) extends DataSlotVal[SensorPair] {
  override var value: SensorPair = SensorPair(default, default.defaultParameter)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    t.setTag(name, Misc.applyMutator(new NBTTagCompound) { tag =>
      tag.setString("uid", value.sensor.uid)
      value.sensor.saveParameter(value.param, tag)
    })
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) = {
    value = SensorRegistry.get(t.getCompoundTag(name).getString("uid"))
      .map(x => SensorPair(x, x.loadParameter(t.getCompoundTag(name))))
      .getOrElse(SensorPair(default, default.defaultParameter))
  }
}
