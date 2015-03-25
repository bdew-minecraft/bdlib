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
import net.bdew.lib.sensors.multiblock.TileRedstoneSensorModule
import net.minecraft.nbt.NBTTagCompound

case class DataSlotSensor[T, R](registry: SensorSystem[T, R], name: String, parent: TileRedstoneSensorModule, default: GenericSensorType[T, R]) extends DataSlotVal[SensorPair[T, R]] {
  override var value = SensorPair(default, default.defaultParameter)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) = {
    t.setTag(name, Misc.applyMutator(new NBTTagCompound) { tag =>
      tag.setString("uid", value.sensor.uid)
      value.sensor.saveParameter(value.param, tag)
    })
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) = {
    value = registry.get(t.getCompoundTag(name).getString("uid"))
      .map(x => SensorPair(x, x.loadParameter(t.getCompoundTag(name))))
      .getOrElse(SensorPair(default, default.defaultParameter))
  }

  def ensureValid(obj: T): Unit = {
    if (!value.sensor.isValidParameter(value.param, obj))
      update(value.copy(param = value.sensor.defaultParameter))
  }
}
