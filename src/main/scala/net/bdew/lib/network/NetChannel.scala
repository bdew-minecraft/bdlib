package net.bdew.lib.network

import net.bdew.lib.Misc
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraftforge.network.simple.SimpleChannel
import net.minecraftforge.network.{NetworkDirection, NetworkEvent, NetworkRegistry, PacketDistributor}
import org.apache.logging.log4j.{LogManager, Logger}

import java.util.Optional
import java.util.function.Supplier
import scala.reflect.ClassTag

class NetChannel(val modId: String, val name: String, val version: String) {
  val log: Logger = LogManager.getLogger

  val id = new ResourceLocation(modId, name)

  val channel: SimpleChannel = NetworkRegistry.newSimpleChannel(id, () => version, version.equals, version.equals)

  type Message = BaseMessage[this.type]

  abstract class Codec[M <: Message : ClassTag] {
    val cls: Class[M] = implicitly[ClassTag[M]].runtimeClass.asInstanceOf[Class[M]]
    def encodeMsg(m: M, p: FriendlyByteBuf): Unit
    def decodeMsg(p: FriendlyByteBuf): M
  }

  def init(): Unit = {
    log.info(s"Initialized network channel '$name' for mod '$modId'")
  }

  def wrapHandler(cs: Supplier[NetworkEvent.Context], h: => Unit): Unit = {
    try {
      h
    } catch {
      case e: Throwable =>
        val ctx = cs.get()
        log.error(s"Error handling packet on channel $id from ${if (ctx.getSender == null) "SERVER" else ctx.getSender.toString}", e)
    } finally {
      cs.get().setPacketHandled(true)
    }
  }

  def regServerHandler[M <: Message](id: Int, codec: Codec[M])(handler: (M, NetworkEvent.Context) => Unit): Unit = {
    channel.registerMessage[M](id, codec.cls, codec.encodeMsg, codec.decodeMsg,
      (m, cs) => wrapHandler(cs, handler(m, cs.get())), Optional.of(NetworkDirection.PLAY_TO_SERVER))
  }

  def regServerContainerHandler[M <: Message, C <: AbstractContainerMenu](id: Int, codec: Codec[M], cont: Class[C])(handler: (M, C, NetworkEvent.Context) => Unit): Unit = {
    regServerHandler(id, codec) { (msg, ctx) =>
      Misc.asInstanceOpt(ctx.getSender.containerMenu, cont) foreach (cont => handler(msg, cont, ctx))
    }
  }

  def regClientHandler[M <: Message](id: Int, codec: Codec[M])(handler: M => Unit): Unit = {
    channel.registerMessage[M](id, codec.cls, codec.encodeMsg, codec.decodeMsg,
      (m, cs) => wrapHandler(cs, handler(m)), Optional.of(NetworkDirection.PLAY_TO_CLIENT))
  }

  def sendToAll(message: Message): Unit = {
    channel.send(PacketDistributor.ALL.noArg(), message)
  }

  def sendTo(message: Message, player: ServerPlayer): Unit = {
    channel.send(PacketDistributor.PLAYER.`with`(() => player), message)
  }

  def sendToAllAround(message: Message, point: PacketDistributor.TargetPoint): Unit = {
    channel.send(PacketDistributor.NEAR.`with`(() => point), message)
  }

  def sendToDimension(message: Message, dimension: ResourceKey[Level]): Unit = {
    channel.send(PacketDistributor.DIMENSION.`with`(() => dimension), message)
  }

  def sendToServer(message: Message): Unit = {
    channel.sendToServer(message)
  }
}
