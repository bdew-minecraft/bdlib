/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.network

import java.util

import cpw.mods.fml.common.network.{FMLEmbeddedChannel, FMLOutboundHandler, NetworkRegistry}
import cpw.mods.fml.relauncher.Side
import io.netty.channel.{ChannelFutureListener, ChannelHandler, ChannelHandlerContext, SimpleChannelInboundHandler}
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer

class NetChannel(val name: String) {
  var channels: util.EnumMap[Side, FMLEmbeddedChannel] = null

  type Message = BaseMessage[this.type]

  object ServerHandler extends SimpleChannelInboundHandler[Message] {
    def channelRead0(ctx: ChannelHandlerContext, msg: Message) {
      try {
        val player = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get.asInstanceOf[NetHandlerPlayServer].playerEntity
        if (serverChain.isDefinedAt(msg, player))
          serverChain(msg, player)
        else
          BdLib.logWarn("Unable to handle message from user %s: %s", player.getDisplayName, msg)
      } catch {
        case e: Throwable => BdLib.logErrorException("Error handling packet", e)
      }
    }
  }

  object ClientHandler extends SimpleChannelInboundHandler[Message] {
    def channelRead0(ctx: ChannelHandlerContext, msg: Message) {
      try {
        if (clientChain.isDefinedAt(msg))
          clientChain(msg)
        else
          BdLib.logWarn("Unable to handle message from server: %s", msg)
      } catch {
        case e: Throwable => BdLib.logErrorException("Error handling packet", e)
      }
    }
  }

  def init() {
    if (channels != null) sys.error("Attempted to initialize a channel twice (%s)".format(name))
    channels = NetworkRegistry.INSTANCE.newChannel(name, new SerializedMessageCodec)
    BdLib.logInfo("Initialized network channel '%s' for mod '%s'", name, Misc.getActiveModId)
    addHandler(Side.SERVER, ServerHandler)
    addHandler(Side.CLIENT, ClientHandler)
  }

  var clientChain = PartialFunction.empty[Message, Unit]
  var serverChain = PartialFunction.empty[(Message, EntityPlayerMP), Unit]

  def regServerHandler(f: PartialFunction[(Message, EntityPlayerMP), Unit]) =
    serverChain = serverChain.orElse(f)

  def regClientHandler(f: PartialFunction[Message, Unit]) =
    clientChain = clientChain.orElse(f)

  private def addHandler(side: Side, handler: ChannelHandler) {
    val ch = channels.get(side)
    val name = ch.findChannelHandlerNameForType(classOf[SerializedMessageCodec[this.type]])
    ch.pipeline().addAfter(name, side + "Handler", handler)
  }

  def sendToAll(message: Message) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendTo(message: Message, player: EntityPlayerMP) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToAllAround(message: Message, point: NetworkRegistry.TargetPoint) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToDimension(message: Message, dimensionId: Int) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new Integer(dimensionId))
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToServer(message: Message) {
    channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER)
    channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

}
