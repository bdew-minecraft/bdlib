package net.bdew.lib.tile

import net.bdew.lib.{Event, Event0}

trait TileTickingServer extends TileExtended {
  val serverTick: Event0 = Event()
}

trait TileTickingClient extends TileExtended {
  val clientTick: Event0 = Event()
}
