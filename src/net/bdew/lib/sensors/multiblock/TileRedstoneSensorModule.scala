/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.sensors.{DataSlotSensor, RedstoneSensors, SensorPair}
import net.bdew.lib.tile.TileTicking
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class TileRedstoneSensorModule(val system: RedstoneSensors[TileEntity], block: BlockRedstoneSensorModule[_]) extends TileModule with TileTicking {
  override val kind = "Sensor"
  override def getCore = getCoreAs[CIRedstoneSensors]

  val config = DataSlotSensor(system, "sensor", this, system.DisabledSensor)

  override def connect(target: TileController) = {
    super.connect(target)
    for {
      core <- Misc.asInstanceOpt(target, classOf[CIRedstoneSensors])
      sensor <- core.redstoneSensorsType.headOption
    } {
      config := SensorPair(sensor, sensor.defaultParameter)
    }
  }
  override def coreRemoved() = {
    config := system.DisabledSensorPair
    super.coreRemoved()
  }

  override def canConnectToCore(bp: BlockPos): Boolean =
    world.getTileSafe[CIRedstoneSensors](bp).exists(_.redstoneSensorSystem == system)

  def isSignalOn = block.getSignal(world, pos)

  serverTick.listen(() => {
    val act = getCore exists config.getResult
    if (block.getSignal(world, pos) != act) {
      block.setSignal(world, pos, act)
    }
  })

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState) =
    oldState.getBlock != newSate.getBlock
}
