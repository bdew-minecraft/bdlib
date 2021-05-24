/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.network

import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.bdew.lib.network.NetChannel

object MultiblockNetHandler extends NetChannel("multiblock", "1") {
  regServerContainerHandler(1, classOf[MsgOutputCfg], classOf[ContainerOutputFaces]) { (msg, cont, _) =>
    cont.te.outputConfig(msg.output).handleConfigPacket(msg.payload)
    cont.te.outputConfig.updated()
  }
}
