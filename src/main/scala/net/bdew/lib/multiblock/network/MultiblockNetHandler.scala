package net.bdew.lib.multiblock.network

import net.bdew.lib.BdLib
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.bdew.lib.network.NetChannel

object MultiblockNetHandler extends NetChannel(BdLib.ModId, "multiblock", "2") {
  private def forwardConfigMsg(m: MsgOutputCfg, cont: ContainerOutputFaces): Unit = {
    cont.te.outputConfig(m.output).handleConfigPacket(m)
    cont.te.outputConfig.updated()
  }

  regServerContainerHandler(1, CodecOutputCfgRSMode, classOf[ContainerOutputFaces]) { (msg, cont, _) =>
    forwardConfigMsg(msg, cont)
  }

  regServerContainerHandler(2, CodecOutputCfgSlot, classOf[ContainerOutputFaces]) { (msg, cont, _) =>
    forwardConfigMsg(msg, cont)
  }
}
