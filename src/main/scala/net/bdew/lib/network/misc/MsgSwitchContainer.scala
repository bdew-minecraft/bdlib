package net.bdew.lib.network.misc

import net.minecraft.network.FriendlyByteBuf

case class MsgSwitchContainer(mode: String) extends MiscNetworkHandler.Message

object CodecSwitchContainer extends MiscNetworkHandler.Codec[MsgSwitchContainer] {
  override def encodeMsg(m: MsgSwitchContainer, p: FriendlyByteBuf): Unit =
    p.writeUtf(m.mode)

  override def decodeMsg(p: FriendlyByteBuf): MsgSwitchContainer =
    MsgSwitchContainer(p.readUtf(100))
}
