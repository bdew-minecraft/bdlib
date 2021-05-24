package net.bdew.lib.network

import io.netty.buffer.{ByteBufInputStream, ByteBufOutputStream}
import net.bdew.lib.{BdLib, Misc}
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.Container
import net.minecraft.network.PacketBuffer
import net.minecraft.util.{RegistryKey, ResourceLocation}
import net.minecraft.world.World
import net.minecraftforge.fml.network.simple.SimpleChannel
import net.minecraftforge.fml.network.{NetworkDirection, NetworkEvent, NetworkRegistry, PacketDistributor}

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.util.Optional

class NetChannel(val name: String, val version: String) {
  val modId: String = Misc.getActiveModId
  val id = new ResourceLocation(modId, name)

  val channel: SimpleChannel = NetworkRegistry.newSimpleChannel(id, () => version, version.equals, version.equals)

  type Message = BaseMessage[this.type]

  private def encodeMsg[M <: Message](m: Message, p: PacketBuffer): Unit = {
    val writer = new ObjectOutputStream(new ByteBufOutputStream(p))
    writer.writeObject(m)
  }

  private def decodeMsg[M <: Message](p: PacketBuffer): M = {
    val reader = new ObjectInputStream(new ByteBufInputStream(p))
    reader.readObject().asInstanceOf[M]
  }

  def init(): Unit = {
    BdLib.logInfo("Initialized network channel '%s' for mod '%s'", name, modId)
  }

  def regServerHandler[M <: Message](id: Int, cls: Class[M])(handler: (M, NetworkEvent.Context) => Unit): Unit = {
    channel.registerMessage[M](id, cls, (m, p) => encodeMsg[M](m, p), m => decodeMsg(m),
      (m, cs) => handler(m, cs.get()), Optional.of(NetworkDirection.PLAY_TO_SERVER))
  }

  def regServerContainerHandler[M <: Message, C <: Container](id: Int, cls: Class[M], cont: Class[C])(handler: (M, C, NetworkEvent.Context) => Unit): Unit = {
    regServerHandler(id, cls) { (msg, ctx) =>
      Misc.asInstanceOpt(ctx.getSender.containerMenu, cont) foreach (cont => handler(msg, cont, ctx))
    }
  }

  def regClientHandler[M <: Message](id: Int, cls: Class[M])(handler: M => Unit): Unit = {
    channel.registerMessage[M](id, cls, (m, p) => encodeMsg[M](m, p), m => decodeMsg(m),
      (m, _) => handler(m), Optional.of(NetworkDirection.PLAY_TO_CLIENT))
  }

  def sendToAll(message: Message): Unit = {
    channel.send(PacketDistributor.ALL.noArg(), message)
  }

  def sendTo(message: Message, player: ServerPlayerEntity): Unit = {
    channel.send(PacketDistributor.PLAYER.`with`(() => player), message)
  }

  def sendToAllAround(message: Message, point: PacketDistributor.TargetPoint): Unit = {
    channel.send(PacketDistributor.NEAR.`with`(() => point), message)
  }

  def sendToDimension(message: Message, dimension: RegistryKey[World]): Unit = {
    channel.send(PacketDistributor.DIMENSION.`with`(() => dimension), message)
  }

  def sendToServer(message: Message): Unit = {
    channel.sendToServer(message)
  }
}
