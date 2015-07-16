/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tooltip

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.bdew.lib.{BdLib, Client}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent

object TooltipHandler {
  var registry = List.empty[TooltipProvider]

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  def register(p: TooltipProvider) = registry :+= p

  @SubscribeEvent
  def handleTooltipEvent(ev: ItemTooltipEvent): Unit = {
    if (ev.itemStack == null || ev.itemStack.getItem == null) return

    val toAdd = for (provider <- registry) yield {
      try {
        if (provider.shouldHandleTooltip(ev.itemStack))
          Some(provider.handleTooltip(ev.itemStack, ev.showAdvancedItemTooltips, Client.shiftDown))
        else
          None
      } catch {
        case t: Throwable =>
          BdLib.logErrorException("Error in tooltip provider %s", t, provider)
          None
      }
    }

    import scala.collection.JavaConversions._

    ev.toolTip ++= toAdd.flatten.flatten
  }
}
