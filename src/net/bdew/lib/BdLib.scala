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
import java.util.logging.Logger
import cpw.mods.fml.common.Mod.EventHandler

@Mod(modid = "bdlib", name = "BD lib", version = "BDLIB_VER", modLanguage = "scala")
object BdLib {
  var log: Logger = null

  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warning(msg.format(args: _*))
  def logError(msg: String, args: Any*) = log.severe(msg.format(args: _*))

  @EventHandler
  def preInit(ev: FMLPreInitializationEvent) {
    log = ev.getModLog
    log.info("bdlib BDLIB_VER loaded")
  }
}
