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
