package net.bdew.lib.network.misc

import net.bdew.lib.BdLib
import net.bdew.lib.container.switchable.SwitchableContainer
import net.bdew.lib.network.NetChannel

object MiscNetworkHandler extends NetChannel(BdLib.ModId, "misc", "1") {
  regServerContainerHandler(1, CodecSwitchContainer, classOf[SwitchableContainer]) { (msg, cont, ctx) =>
    if (!cont.activateModeRemote(msg.mode))
      log.warn(s"Attempting to set invalid mode ${msg.mode} on container ${cont.getClass.getSimpleName} from ${ctx.getSender}")
  }
}
