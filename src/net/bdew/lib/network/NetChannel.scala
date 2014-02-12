/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/teleporter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/teleporter/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import cpw.mods.fml.common.network.{FMLOutboundHandler, NetworkRegistry}
import cpw.mods.fml.relauncher.Side
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayerMP
import io.netty.channel.{ChannelHandler, ChannelFutureListener}

class NetChannel(val name: String) {
  val channels = NetworkRegistry.INSTANCE.newChannel(name, new NBTMessageToMessageCodec)

  var serverHandlers = Map.empty[String, (NBTTagCompound, EntityPlayerMP) => Unit]
  var clientHandlers = Map.empty[String, (NBTTagCompound) => Unit]

  def regServerHandler(id: String, callable: (NBTTagCompound, EntityPlayerMP) => Unit) =
    if (serverHandlers.contains(id))
      sys.error("Command name already in use: %s".format(name))
    else
      serverHandlers += (id -> callable)

  def regClientHandler(id: String, callable: (NBTTagCompound) => Unit) =
    if (clientHandlers.contains(id))
      sys.error("Command name already in use: %s".format(name))
    else
      clientHandlers += (id -> callable)

  addHandler(Side.SERVER, new NetChannelServerHandler(this))
  addHandler(Side.CLIENT, new NetChannelClientHandler(this))

  def addHandler(side: Side, handler: ChannelHandler) {
    val ch = channels.get(side)
    val name = ch.findChannelHandlerNameForType(classOf[NBTMessageToMessageCodec])
    ch.pipeline().addAfter(name, side + "Handler", handler)
  }

  def sendToAll(message: NBTTagCompound) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendTo(message: NBTTagCompound, player: EntityPlayerMP) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToAllAround(message: NBTTagCompound, point: NetworkRegistry.TargetPoint) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point)
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToDimension(message: NBTTagCompound, dimensionId: Int) {
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION)
    channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new Integer(dimensionId))
    channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

  def sendToServer(message: NBTTagCompound) {
    channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER)
    channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
  }

}
