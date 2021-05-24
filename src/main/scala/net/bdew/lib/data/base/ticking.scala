package net.bdew.lib.data.base

import net.bdew.lib.Event0
import net.bdew.lib.tile.TileTicking

/**
 * Trait for container that support ticks.
 * Register handler with server/clientTick.listen(...)
 */
trait DataSlotContainerTicking extends DataSlotContainer {
  val serverTick: Event0
  val clientTick: Event0
}

/**
 * Trait for data slots that need ticks
 */
trait DataSlotTicking extends DataSlot {
  val parent: DataSlotContainerTicking
}

trait TileDataSlotsTicking extends TileDataSlots with TileTicking with DataSlotContainerTicking

