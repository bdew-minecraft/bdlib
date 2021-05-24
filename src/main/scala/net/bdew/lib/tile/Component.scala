package net.bdew.lib.tile

trait Component {
  def added(p: TileExtended, name: String): Unit
}
