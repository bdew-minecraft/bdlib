/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors

import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.bdew.lib.nbt.NBT
import net.bdew.lib.sensors.multiblock.TileRedstoneSensorModule
import net.minecraft.nbt.CompoundNBT

case class DataSlotSensor[T, R](registry: SensorSystem[T, R], name: String, parent: TileRedstoneSensorModule, sensorType: GenericSensorType[T, R]) extends DataSlotVal[SensorPair[T, R]] {
  override def default: SensorPair[T, R] = SensorPair(sensorType, sensorType.defaultParameter)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    t.put(name, NBT.from { tag =>
      tag.putString("uid", value.sensor.uid)
      value.sensor.saveParameter(value.param, tag)
    })
  }

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): SensorPair[T, R] = {
    registry.get(t.getCompound(name).getString("uid"))
      .map(x => SensorPair(x, x.loadParameter(t.getCompound(name))))
      .getOrElse(default)
  }

  def ensureValid(obj: T): Unit = {
    if (!value.sensor.isValidParameter(value.param, obj))
      update(value.copy(param = value.sensor.defaultParameter))
  }
}
