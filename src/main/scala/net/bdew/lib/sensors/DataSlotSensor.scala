package net.bdew.lib.sensors

import net.bdew.lib.data.base.{DataSlotVal, UpdateKind}
import net.bdew.lib.nbt.NBT
import net.bdew.lib.sensors.multiblock.TileRedstoneSensorModule
import net.minecraft.nbt.CompoundTag

case class DataSlotSensor[T, R](registry: SensorSystem[T, R], name: String, parent: TileRedstoneSensorModule, sensorType: GenericSensorType[T, R]) extends DataSlotVal[SensorPair[T, R]] {
  override def default: SensorPair[T, R] = SensorPair(sensorType, sensorType.defaultParameter)

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.put(name, NBT.from { tag =>
      tag.putString("uid", value.sensor.uid)
      value.sensor.saveParameter(value.param, tag)
    })
  }

  override def loadValue(t: CompoundTag, kind: UpdateKind.Value): SensorPair[T, R] = {
    registry.get(t.getCompound(name).getString("uid"))
      .map(x => SensorPair(x, x.loadParameter(t.getCompound(name))))
      .getOrElse(default)
  }
}
