package net.bdew.lib.multiblock.network

import net.minecraft.network.FriendlyByteBuf

case class MsgOutputCfgSlot(output: Int, slot: String) extends MultiblockNetHandler.Message with MsgOutputCfg

object CodecOutputCfgSlot extends MultiblockNetHandler.Codec[MsgOutputCfgSlot] {
  override def encodeMsg(m: MsgOutputCfgSlot, p: FriendlyByteBuf): Unit = {
    p.writeVarInt(m.output)
    p.writeUtf(m.slot)
  }

  override def decodeMsg(p: FriendlyByteBuf): MsgOutputCfgSlot = {
    MsgOutputCfgSlot(p.readVarInt(), p.readUtf(100))
  }
}
