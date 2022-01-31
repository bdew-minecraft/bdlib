package net.bdew.lib.sensors.multiblock

import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.sensors.{GenericSensorType, SensorSystem}
import net.minecraft.world.level.block.entity.BlockEntity

trait CIRedstoneSensors extends TileController {
  def redstoneSensorsType: Seq[GenericSensorType[BlockEntity, Boolean]]
  def redstoneSensorSystem: SensorSystem[BlockEntity, Boolean]
}
