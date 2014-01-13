/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent

@Mod(modid = "bdlib", name = "BD lib", version = "BDLIB_VER", modLanguage = "scala")
object BdLib {
  def preInit(ev: FMLPreInitializationEvent) {
    ev.getModLog.info("bdlib BDLIB_VER loaded")
  }
}
