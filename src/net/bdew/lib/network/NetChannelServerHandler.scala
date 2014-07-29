/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import cpw.mods.fml.common.network.NetworkRegistry
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import net.bdew.lib.BdLib
import net.minecraft.network.NetHandlerPlayServer

class NetChannelServerHandler(ch: NetChannel) extends SimpleChannelInboundHandler[Message] {
  def channelRead0(ctx: ChannelHandlerContext, msg: Message) {
    try {
      val player = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get.asInstanceOf[NetHandlerPlayServer].playerEntity
      if (ch.serverChain.isDefinedAt(msg, player))
        ch.serverChain(msg, player)
      else
        BdLib.logWarn("Unable to handle message from user %s: %s", player.getDisplayName, msg)
    } catch {
      case e: Throwable => BdLib.logErrorException("Error handling packet", e)
    }
  }
}
