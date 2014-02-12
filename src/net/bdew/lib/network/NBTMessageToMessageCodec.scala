/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/teleporter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/teleporter/master/MMPL-1.0.txt
 */

package net.bdew.lib.network

import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import net.minecraft.nbt.{CompressedStreamTools, NBTTagCompound}
import io.netty.channel.ChannelHandlerContext
import java.util
import java.io.{DataInputStream, DataOutputStream}
import io.netty.buffer.{ByteBufInputStream, ByteBufOutputStream, Unpooled}
import cpw.mods.fml.common.network.NetworkRegistry
import io.netty.channel.ChannelHandler.Sharable
import net.bdew.lib.BdLib

@Sharable
class NBTMessageToMessageCodec extends MessageToMessageCodec[FMLProxyPacket, NBTTagCompound] {
  def encode(ctx: ChannelHandlerContext, msg: NBTTagCompound, out: util.List[AnyRef]) {
    val buff = Unpooled.buffer()
    val writer = new DataOutputStream(new ByteBufOutputStream(buff))
    CompressedStreamTools.write(msg, writer)
    val pkt = new FMLProxyPacket(buff, ctx.channel.attr(NetworkRegistry.FML_CHANNEL).get)
    out.add(pkt)
  }
  def decode(ctx: ChannelHandlerContext, msg: FMLProxyPacket, out: util.List[AnyRef]) {
    try {
      val stream = new DataInputStream(new ByteBufInputStream(msg.payload()))
      out.add(CompressedStreamTools.read(stream))
    } catch {
      case e: Throwable => BdLib.log.error("Error decoding packet", e)
    }
  }
}
