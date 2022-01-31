package net.bdew.lib.data.base

import net.bdew.lib.Event0
import net.bdew.lib.tile.TileTickingServer

/**
 * Trait for container that support server side ticks
 * client ticks support removed as of 1.18
 * Register handler with server.listen(...)
 */
trait DataSlotContainerTicking extends DataSlotContainer {
  val serverTick: Event0
}

/**
 * Trait for data slots that need ticks
 */
trait DataSlotTicking extends DataSlot {
  val parent: DataSlotContainerTicking
}

trait TileDataSlotsTicking extends TileDataSlots with TileTickingServer with DataSlotContainerTicking

