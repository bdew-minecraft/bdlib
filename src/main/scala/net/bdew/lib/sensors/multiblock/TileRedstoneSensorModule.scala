package net.bdew.lib.sensors.multiblock

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.sensors.{DataSlotSensor, RedstoneSensors, SensorPair}
import net.bdew.lib.tile.{TileExtended, TileTicking}
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.math.BlockPos

abstract class TileRedstoneSensorModule(val kind: ModuleType, val system: RedstoneSensors[TileEntity], block: BlockRedstoneSensorModule[_], teType: TileEntityType[_]) extends TileExtended(teType)
  with TileModule with TileTicking with INamedContainerProvider {

  override def getCore: Option[CIRedstoneSensors] = getCoreAs[CIRedstoneSensors]

  val config: DataSlotSensor[TileEntity, Boolean] = DataSlotSensor(system, "sensor", this, system.DisabledSensor)

  override def connect(target: TileController): Unit = {
    super.connect(target)
    for {
      core <- Misc.asInstanceOpt(target, classOf[CIRedstoneSensors])
      sensor <- core.redstoneSensorsType.headOption
    } {
      config := SensorPair(sensor, sensor.defaultParameter)
    }
  }
  override def coreRemoved(): Unit = {
    config := system.DisabledSensorPair
    super.coreRemoved()
  }

  override def canConnectToCore(bp: BlockPos): Boolean =
    getLevel.getTileSafe[CIRedstoneSensors](bp).exists(_.redstoneSensorSystem == system)

  def isSignalOn: Boolean = block.getSignal(getLevel, getBlockPos)

  serverTick.listen(() => {
    val act = getCore exists config.getResult
    if (block.getSignal(getLevel, getBlockPos) != act) {
      block.setSignal(getLevel, getBlockPos, act)
    }
  })
}
