package net.bdew.lib.sensors.multiblock

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.sensors.{DataSlotSensor, RedstoneSensors, SensorPair}
import net.bdew.lib.tile.{TileExtended, TileTickingServer}
import net.minecraft.core.BlockPos
import net.minecraft.world.MenuProvider
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState

abstract class TileRedstoneSensorModule(val kind: ModuleType, val system: RedstoneSensors[BlockEntity], block: BlockRedstoneSensorModule[_], teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state)
  with TileModule with TileTickingServer with MenuProvider {

  override def getCore: Option[CIRedstoneSensors] = getCoreAs[CIRedstoneSensors]

  val config: DataSlotSensor[BlockEntity, Boolean] = DataSlotSensor(system, "sensor", this, system.DisabledSensor)

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
