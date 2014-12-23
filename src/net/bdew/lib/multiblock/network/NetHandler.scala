/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.network

import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.bdew.lib.network.NetChannel
import net.minecraft.entity.player.EntityPlayerMP

object NetHandler extends NetChannel("bdew.multiblock") {
  regServerHandler {
    case (msg: MsgOutputCfg, p: EntityPlayerMP) =>
      val te = p.openContainer.asInstanceOf[ContainerOutputFaces].te
      te.outputConfig(msg.output).handleConfigPacket(msg)
      te.outputConfig.updated()
  }
}
