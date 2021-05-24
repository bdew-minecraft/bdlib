package net.bdew.lib.gui

import net.bdew.lib.gui

object Direction extends Enumeration {
  type Direction = Value
  val UP: gui.Direction.Value = Value("UP")
  val DOWN: gui.Direction.Value = Value("DOWN")
  val LEFT: gui.Direction.Value = Value("LEFT")
  val RIGHT: gui.Direction.Value = Value("RIGHT")
}
