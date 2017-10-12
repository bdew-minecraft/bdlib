/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.bdew.lib.capabilities.CapAdapters
import net.bdew.lib.multiblock.network.NetHandler
import net.bdew.lib.render.ColorHandlers
import net.bdew.lib.tooltip.TooltipHandler
import net.minecraft.command.CommandHandler
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent, FMLServerStoppingEvent}
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod}
import org.apache.logging.log4j.Logger

@Mod(modid = "bdlib", name = "BD lib", version = "BDLIB_VER", modLanguage = "scala", acceptedMinecraftVersions = "[1.12,1.12.2]")
object BdLib {
  var log: Logger = _

  def logDebug(msg: String, args: Any*) = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*) = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*) = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*) = log.error(msg.format(args: _*), t)

  val onServerStarting = Event[FMLServerStartingEvent]
  val onServerStopping = Event[FMLServerStoppingEvent]

  @EventHandler
  def preInit(ev: FMLPreInitializationEvent) {
    log = ev.getModLog
    log.info("bdlib BDLIB_VER loaded")
    log.debug("List of loaded APIs: " + ApiReporter.APIs)
    FMLCommonHandler.instance().registerCrashCallable(ApiReporter)
    NetHandler.init()
    CapAdapters.init()
    if (ev.getSide.isClient) {
      TooltipHandler.init()
    }
  }

  @EventHandler
  def init(ev: FMLInitializationEvent): Unit = {
    if (ev.getSide.isClient) {
      ColorHandlers.addDelayed()
    }
  }

  @EventHandler
  def serverStarting(event: FMLServerStartingEvent) {
    val commandHandler = event.getServer.getCommandManager.asInstanceOf[CommandHandler]
    commandHandler.registerCommand(CommandDumpRegistry)
    commandHandler.registerCommand(CommandOreDistribution)
    onServerStarting.trigger(event)
  }

  @EventHandler
  def serverStopping(event: FMLServerStoppingEvent) {
    onServerStopping.trigger(event)
  }
}
