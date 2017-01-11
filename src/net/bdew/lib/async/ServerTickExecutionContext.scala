/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.async

import net.bdew.lib.BdLib
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent

import scala.collection.mutable
import scala.concurrent.ExecutionContext

object ServerTickExecutionContext extends ExecutionContext {
  private val queue = mutable.Queue.empty[Runnable]

  MinecraftForge.EVENT_BUS.register(this)

  BdLib.logDebug("Installed ServerTickExecutionContext")

  private def getNextBatch = this.synchronized {
    if (queue.nonEmpty) {
      val res = queue.toList
      queue.clear()
      res
    } else List.empty
  }

  override def execute(runnable: Runnable): Unit =
    this.synchronized {
      queue += runnable
    }

  override def reportFailure(cause: Throwable): Unit =
    BdLib.logErrorException("Error in execution context", cause)

  def doSingleLoop(): Unit = {
    for (r <- getNextBatch) {
      try {
        r.run()
      } catch {
        case t: Throwable => reportFailure(t)
      }
    }
  }

  @SubscribeEvent
  def onTick(ev: ServerTickEvent): Unit = doSingleLoop()
}
