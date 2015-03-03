/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.sensors.DataSlotSensor

abstract class TileSensorModule extends TileModule {
  def config: DataSlotSensor
  override def getCore = getCoreAs[CISensors]
}
