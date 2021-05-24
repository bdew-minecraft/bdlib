package net.bdew.lib.sensors.multiblock

import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.sensors.{GenericSensorType, SensorSystem}
import net.minecraft.tileentity.TileEntity

trait CIRedstoneSensors extends TileController {
  def redstoneSensorsType: Seq[GenericSensorType[TileEntity, Boolean]]
  def redstoneSensorSystem: SensorSystem[TileEntity, Boolean]
}
