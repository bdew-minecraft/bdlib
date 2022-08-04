package net.bdew.lib

import net.bdew.lib.commands.{CommandDumpRegistry, CommandOreDistribution}
import net.bdew.lib.container.ContainerEventListener
import net.bdew.lib.multiblock.network.MultiblockNetHandler
import net.bdew.lib.network.misc.MiscNetworkHandler
import net.bdew.lib.registries.LootFunctionTypes
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.{LogManager, Logger}


@Mod(BdLib.ModId)
class BdLib {
  MinecraftForge.EVENT_BUS.addListener(this.registerCommands)

  LootFunctionTypes.init()

  MultiblockNetHandler.init()
  MiscNetworkHandler.init()

  ContainerEventListener.register()

  private def registerCommands(event: RegisterCommandsEvent): Unit = {
    event.getDispatcher.register(CommandOreDistribution.register)
    event.getDispatcher.register(CommandDumpRegistry.register)
  }
}

object BdLib {
  final val ModId = "bdlib"

  val log: Logger = LogManager.getLogger

  def logDebug(msg: String, args: Any*): Unit = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*): Unit = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*): Unit = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*): Unit = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*): Unit = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*): Unit = log.error(msg.format(args: _*), t)
}