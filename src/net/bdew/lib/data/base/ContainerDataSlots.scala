/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.data.base

import net.bdew.lib.gui.NoInvContainer
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.inventory.IContainerListener

trait ContainerDataSlots extends NoInvContainer {
  val dataSource: TileDataSlots
  var lastSentChange = dataSource.lastChange

  override def addListener(crafter: IContainerListener) {
    super.addListener(crafter)
    if (crafter.isInstanceOf[EntityPlayerMP]) {
      val pkt = dataSource.getDataSlotPacket
      crafter.asInstanceOf[EntityPlayerMP].connection.sendPacket(pkt)
    }
  }

  override def detectAndSendChanges() {
    super.detectAndSendChanges()
    // only send updates every 10 ticks
    if (dataSource.lastChange >= lastSentChange + 10 || (dataSource.lastChange > lastSentChange && lastSentChange + 10 < dataSource.getWorld.getTotalWorldTime)) {
      lastSentChange = dataSource.lastChange
      val pkt = dataSource.getDataSlotPacket

      for (player <- players)
        player.connection.sendPacket(pkt)
    }
  }

  override def canInteractWith(player: EntityPlayer) = dataSource.isEntityInRange(player, 64D)
}
