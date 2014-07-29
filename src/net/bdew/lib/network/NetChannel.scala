/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import java.util

import cpw.mods.fml.common.network.{FMLEmbeddedChannel, FMLOutboundHandler, NetworkRegistry}
import cpw.mods.fml.relauncher.Side
import io.netty.channel.{ChannelFutureListener, ChannelHandler}
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.entity.player.EntityPlayerMP

class NetChannel(val name: String) {
  var channels: util.EnumMap[Side, FMLEmbeddedChannel] = null

  def init() {
    channels = NetworkRegistry.INSTANCE.newChannel(name, new SerializedMessageCodec)
    BdLib.logInfo("Initialized network channel '%s' for mod '%s'", name, Misc.getActiveModId)
    addHandler(Side.SERVER, new NetChannelServerHandler(this))
    addHandler(Side.CLIENT, new NetChannelClientHandler(this))
  }

  var clientChain = PartialFunction.empty[Message, Unit]
  var serverChain = PartialFunction.empty[(Message, EntityPlayerMP), Unit]

  def regServerHandler(f: PartialFunction[(Message, EntityPlayerMP), Unit]) =
    serverChain = serverChain.orElse(f)

  def regClientHandler(f: PartialFunction[Message, Unit]) =
    clientChain = clientChain.orElse(f)

  private def addHandler(side: Side, handler: ChannelHandler) {
    val ch = channels.get(side)
    val name = ch.findChannelHandlerNameForType(classOf[SerializedMessageCodec])
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
