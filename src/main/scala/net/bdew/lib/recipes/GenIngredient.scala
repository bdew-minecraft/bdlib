package net.bdew.lib.recipes

import net.bdew.lib.misc.Taggable
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraftforge.registries.ForgeRegistryEntry

sealed abstract class GenIngredient[T <: ForgeRegistryEntry[T] : Taggable] {
  def test(other: T): Boolean
  def toPacket(pkt: FriendlyByteBuf): Unit
  def resolve: Set[T]
  def isEmpty: Boolean
}

case class GenIngredientSimple[T <: ForgeRegistryEntry[T] : Taggable](v: T) extends GenIngredient[T] {
  override def test(other: T): Boolean = v == other || v.getRegistryName == other.getRegistryName
  override def resolve: Set[T] = Set(v)
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(0)
    pkt.writeUtf(v.getRegistryName.toString)
  }
  override def isEmpty: Boolean = false
}

case class GenIngredientTag[T <: ForgeRegistryEntry[T] : Taggable](v: TagKey[T]) extends GenIngredient[T] {
  override def test(other: T): Boolean = Taggable[T].is(other, v)
  override def resolve: Set[T] = Taggable[T].resolve(v)
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(1)
    pkt.writeUtf(v.location().toString)
  }
  override def isEmpty: Boolean = false
}

case class GenIngredientEmpty[T <: ForgeRegistryEntry[T] : Taggable]() extends GenIngredient[T] {
  override def test(other: T): Boolean = false
  override def resolve: Set[T] = Set.empty
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(2)
  }
  override def isEmpty: Boolean = true
}

object GenIngredient {
  def fromPacket[T <: ForgeRegistryEntry[T] : Taggable](pkt: FriendlyByteBuf): GenIngredient[T] = {
    pkt.readByte() match {
      case 0 => of(Taggable[T].registry.getValue(new ResourceLocation(pkt.readUtf())))
      case 1 => of(Taggable[T].createTag(new ResourceLocation(pkt.readUtf())))
      case 2 => empty[T]
      case x => throw new RuntimeException(s"Invalid generic ingredient type $x")
    }
  }

  def of[T <: ForgeRegistryEntry[T] : Taggable](v: T): GenIngredient[T] = GenIngredientSimple(v)
  def of[T <: ForgeRegistryEntry[T] : Taggable](v: TagKey[T]): GenIngredient[T] = GenIngredientTag(v)
  def empty[T <: ForgeRegistryEntry[T] : Taggable]: GenIngredientEmpty[T] = GenIngredientEmpty[T]()
}