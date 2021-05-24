/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.gui

object Direction extends Enumeration {
  type Direction = Value
  val UP: gui.Direction.Value = Value("UP")
  val DOWN: gui.Direction.Value = Value("DOWN")
  val LEFT: gui.Direction.Value = Value("LEFT")
  val RIGHT: gui.Direction.Value = Value("RIGHT")
}
