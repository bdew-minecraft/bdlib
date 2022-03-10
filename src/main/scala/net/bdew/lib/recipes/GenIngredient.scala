package net.bdew.lib.recipes

import net.bdew.lib.misc.Tagable
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraftforge.registries.ForgeRegistryEntry

sealed abstract class GenIngredient[T <: ForgeRegistryEntry[T] : Tagable] {
  def test(other: T): Boolean
  def toPacket(pkt: FriendlyByteBuf): Unit
}

case class GenIngredientSimple[T <: ForgeRegistryEntry[T] : Tagable](v: T) extends GenIngredient[T] {
  override def test(other: T): Boolean = v == other || v.getRegistryName == other.getRegistryName
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(0)
    pkt.writeUtf(v.getRegistryName.toString)
  }
}

case class GenIngredientTag[T <: ForgeRegistryEntry[T] : Tagable](v: TagKey[T]) extends GenIngredient[T] {
  override def test(other: T): Boolean = Tagable[T].is(other, v)
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(1)
    pkt.writeUtf(v.location().toString)
  }
}

object GenIngredient {
  def fromPacket[T <: ForgeRegistryEntry[T] : Tagable](pkt: FriendlyByteBuf): GenIngredient[T] = {
    pkt.readByte() match {
      case 0 => GenIngredientSimple[T](Tagable[T].registry.getValue(new ResourceLocation(pkt.readUtf())))
      case 1 => GenIngredientTag[T](Tagable[T].createTag(new ResourceLocation(pkt.readUtf())))
      case x => throw new RuntimeException(s"Invalid generic ingredient type $x")
    }
  }

  def of[T <: ForgeRegistryEntry[T] : Tagable](v: T): GenIngredient[T] = GenIngredientSimple(v)
  def of[T <: ForgeRegistryEntry[T] : Tagable](v: TagKey[T]): GenIngredient[T] = GenIngredientTag(v)
}