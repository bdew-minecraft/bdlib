/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

object UpdateKind extends Enumeration {
  val WORLD = Value("WORLD")
  val GUI = Value("GUI")
  val SAVE = Value("SAVE")
  val RENDER = Value("RENDER")
}