package net.bdew.lib

import net.bdew.lib.commands.{CommandDumpRegistry, CommandOreDistribution}
import net.bdew.lib.keepdata.KeepDataLootFunction
import net.bdew.lib.multiblock.network.MultiblockNetHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(BdLib.ModId)
object BdLib {
  final val ModId = "bdlib"

  val log: Logger = LogManager.getLogger

  def logDebug(msg: String, args: Any*): Unit = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*): Unit = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*): Unit = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*): Unit = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*): Unit = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*): Unit = log.error(msg.format(args: _*), t)

  MinecraftForge.EVENT_BUS.addListener(this.registerCommands)

  MultiblockNetHandler.init()

  KeepDataLootFunction.register()

  private def registerCommands(event: RegisterCommandsEvent): Unit = {
    event.getDispatcher.register(CommandOreDistribution.register)
    event.getDispatcher.register(CommandDumpRegistry.register)
  }
}
