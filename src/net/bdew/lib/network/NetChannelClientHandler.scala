/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import net.bdew.lib.BdLib

class NetChannelClientHandler(ch: NetChannel) extends SimpleChannelInboundHandler[Message] {
  def channelRead0(ctx: ChannelHandlerContext, msg: Message) {
    try {
      if (ch.clientChain.isDefinedAt(msg))
        ch.clientChain(msg)
      else
        BdLib.logWarn("Unable to handle message from server: %s", msg)
    } catch {
      case e: Throwable => BdLib.logErrorException("Error handling packet", e)
    }
  }
}
