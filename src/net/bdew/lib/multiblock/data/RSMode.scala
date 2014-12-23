/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

object RSMode extends Enumeration {
  val ALWAYS = Value(0, "ALWAYS")
  val RS_ON = Value(1, "RS_ON")
  val RS_OFF = Value(2, "RS_OFF")
  val NEVER = Value(3, "NEVER")
}
