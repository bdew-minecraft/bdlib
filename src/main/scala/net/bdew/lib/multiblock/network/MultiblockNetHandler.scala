package net.bdew.lib.multiblock.network

import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.bdew.lib.network.NetChannel

object MultiblockNetHandler extends NetChannel("multiblock", "1") {
  regServerContainerHandler(1, classOf[MsgOutputCfg], classOf[ContainerOutputFaces]) { (msg, cont, _) =>
    cont.te.outputConfig(msg.output).handleConfigPacket(msg.payload)
    cont.te.outputConfig.updated()
  }
}
