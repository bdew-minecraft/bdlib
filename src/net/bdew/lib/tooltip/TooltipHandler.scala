/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tooltip

import net.bdew.lib.{BdLib, Client}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TooltipHandler {
  var registry = List.empty[TooltipProvider]

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  def register(p: TooltipProvider) = registry :+= p

  @SubscribeEvent
  def handleTooltipEvent(ev: ItemTooltipEvent): Unit = {
    if (ev.getItemStack.isEmpty) return

    val toAdd = for (provider <- registry) yield {
      try {
        if (provider.shouldHandleTooltip(ev.getItemStack))
          Some(provider.handleTooltip(ev.getItemStack, ev.getFlags.isAdvanced, Client.shiftDown))
        else
          None
      } catch {
        case t: Throwable =>
          BdLib.logErrorException("Error in tooltip provider %s", t, provider)
          None
      }
    }

    import scala.collection.JavaConversions._

    ev.getToolTip ++= toAdd.flatten.flatten
  }
}
