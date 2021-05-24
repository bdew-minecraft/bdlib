/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile

import net.bdew.lib.{Event, Event0}
import net.minecraft.tileentity.ITickableTileEntity

trait TileTicking extends TileExtended with ITickableTileEntity {
  val clientTick: Event0 = Event()
  val serverTick: Event0 = Event()

  override def tick(): Unit = {
    if (getLevel != null) {
      if (getLevel.isClientSide) {
        clientTick.trigger()
      } else {
        serverTick.trigger()
      }
    }
  }
}
