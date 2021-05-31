package net.bdew.lib.multiblock.network

import net.bdew.lib.misc.RSMode
import net.minecraft.network.PacketBuffer

case class MsgOutputCfgRSMode(output: Int, rsMode: RSMode.Value) extends MultiblockNetHandler.Message with MsgOutputCfg

object CodecOutputCfgRSMode extends MultiblockNetHandler.Codec[MsgOutputCfgRSMode] {
  override def encodeMsg(m: MsgOutputCfgRSMode, p: PacketBuffer): Unit = {
    p.writeVarInt(m.output)
    p.writeByte(m.rsMode.id)
  }

  override def decodeMsg(p: PacketBuffer): MsgOutputCfgRSMode =
    MsgOutputCfgRSMode(p.readVarInt(), RSMode(p.readByte()))
}
