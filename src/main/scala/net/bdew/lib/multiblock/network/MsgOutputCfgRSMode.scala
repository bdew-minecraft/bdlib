package net.bdew.lib.multiblock.network

import net.bdew.lib.misc.RSMode
import net.minecraft.network.FriendlyByteBuf

case class MsgOutputCfgRSMode(output: Int, rsMode: RSMode.Value) extends MultiblockNetHandler.Message with MsgOutputCfg

object CodecOutputCfgRSMode extends MultiblockNetHandler.Codec[MsgOutputCfgRSMode] {
  override def encodeMsg(m: MsgOutputCfgRSMode, p: FriendlyByteBuf): Unit = {
    p.writeVarInt(m.output)
    p.writeByte(m.rsMode.id)
  }

  override def decodeMsg(p: FriendlyByteBuf): MsgOutputCfgRSMode =
    MsgOutputCfgRSMode(p.readVarInt(), RSMode(p.readByte()))
}
