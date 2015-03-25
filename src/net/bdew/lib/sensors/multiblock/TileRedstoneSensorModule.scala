/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.sensors.{DataSlotSensor, RedstoneSensors, SensorPair}
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

abstract class TileRedstoneSensorModule(val system: RedstoneSensors[TileEntity], block: BlockRedstoneSensorModule[_]) extends TileModule {
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

  override def canConnectToCore(br: BlockRef): Boolean =
    br.getTile[CIRedstoneSensors](worldObj).exists(_.redstoneSensorSystem == system)

  def isSignalOn = block.isSignalOn(worldObj, xCoord, yCoord, zCoord)

  serverTick.listen(() => {
    val act = getCore exists config.getResult
    if (block.isSignalOn(worldObj, xCoord, yCoord, zCoord) != act) {
      block.setSignal(worldObj, xCoord, yCoord, zCoord, act)
    }
  })

  override def shouldRefresh(oldBlock: Block, newBlock: Block, oldMeta: Int, newMeta: Int, world: World, x: Int, y: Int, z: Int) =
    newBlock != oldBlock
}
