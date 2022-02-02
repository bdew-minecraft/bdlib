package net.bdew.lib.container

import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerContainerEvent

object ContainerEventListener {
  def register(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(this.containerOpen)
    MinecraftForge.EVENT_BUS.addListener(this.containerClosed)
  }

  private def containerOpen(event: PlayerContainerEvent.Open): Unit = {
    (event.getPlayer, event.getContainer) match {
      case (player: ServerPlayer, cont: NoInvContainer) => cont.playerAdded(player)
      case _ => // no op
    }
  }

  private def containerClosed(event: PlayerContainerEvent.Close): Unit = {
    (event.getPlayer, event.getContainer) match {
      case (player: ServerPlayer, cont: NoInvContainer) => cont.playerRemoved(player)
      case _ => // no op
    }
  }
}
