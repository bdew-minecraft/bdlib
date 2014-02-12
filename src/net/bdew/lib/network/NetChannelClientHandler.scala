/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/teleporter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/teleporter/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import net.minecraft.nbt.NBTTagCompound
import net.bdew.lib.BdLib

class NetChannelClientHandler(ch: NetChannel) extends SimpleChannelInboundHandler[NBTTagCompound] {
  def channelRead0(ctx: ChannelHandlerContext, msg: NBTTagCompound) {
    val cmd = msg.getString("_cmd")
    ch.clientHandlers.getOrElse(cmd, {
      BdLib.log.error("Unknown command: '%s' from server".format(cmd))
      return
    })(msg)
  }
}
