/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.network

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.util

import io.netty.buffer.{ByteBufInputStream, ByteBufOutputStream, Unpooled}
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import net.bdew.lib.BdLib
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket

@Sharable
class SerializedMessageCodec[T <: NetChannel] extends MessageToMessageCodec[FMLProxyPacket, BaseMessage[T]] {
  def encode(ctx: ChannelHandlerContext, msg: BaseMessage[T], out: util.List[AnyRef]) {
    val buff = new PacketBuffer(Unpooled.buffer())
    val writer = new ObjectOutputStream(new ByteBufOutputStream(buff))
    writer.writeObject(msg)
    val pkt = new FMLProxyPacket(buff, ctx.channel.attr(NetworkRegistry.FML_CHANNEL).get)
    out.add(pkt)
  }

  def decode(ctx: ChannelHandlerContext, msg: FMLProxyPacket, out: util.List[AnyRef]) {
    try {
      val reader = new SafeObjectInputStream(new ByteBufInputStream(msg.payload()))
      out.add(reader.readObject())
    } catch {
      case e: Throwable => BdLib.log.error("Error decoding packet", e)
    }
  }
}
