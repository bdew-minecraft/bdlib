/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

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

trait TileDataSlotsTicking extends TileDataSlots with DataSlotContainerTicking with TileTicking

