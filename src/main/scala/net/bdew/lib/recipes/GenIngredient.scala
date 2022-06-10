package net.bdew.lib.recipes

import net.bdew.lib.misc.Taggable
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

sealed abstract class GenIngredient[T: Taggable] {
  def test(other: T): Boolean
  def toPacket(pkt: FriendlyByteBuf): Unit
  def resolve: Set[T]
  def isEmpty: Boolean
}

case class GenIngredientSimple[T: Taggable](v: T) extends GenIngredient[T] {
  private val registry = Taggable[T].registry
  override def test(other: T): Boolean = v == other || registry.getKey(v) == registry.getKey(other)
  override def resolve: Set[T] = Set(v)
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(0)
    pkt.writeUtf(registry.getKey(v).toString)
  }
  override def isEmpty: Boolean = false
}

case class GenIngredientTag[T: Taggable](v: TagKey[T]) extends GenIngredient[T] {
  override def test(other: T): Boolean = Taggable[T].is(other, v)
  override def resolve: Set[T] = Taggable[T].resolve(v)
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(1)
    pkt.writeUtf(v.location().toString)
  }
  override def isEmpty: Boolean = false
}

case class GenIngredientEmpty[T: Taggable]() extends GenIngredient[T] {
  override def test(other: T): Boolean = false
  override def resolve: Set[T] = Set.empty
  override def toPacket(pkt: FriendlyByteBuf): Unit = {
    pkt.writeByte(2)
  }
  override def isEmpty: Boolean = true
}

object GenIngredient {
  def fromPacket[T: Taggable](pkt: FriendlyByteBuf): GenIngredient[T] = {
    pkt.readByte() match {
      case 0 => of(Taggable[T].registry.getValue(new ResourceLocation(pkt.readUtf())))
      case 1 => of(Taggable[T].createTag(new ResourceLocation(pkt.readUtf())))
      case 2 => empty[T]
      case x => throw new RuntimeException(s"Invalid generic ingredient type $x")
    }
  }

  def of[T: Taggable](v: T): GenIngredient[T] = GenIngredientSimple(v)
  def of[T: Taggable](v: TagKey[T]): GenIngredient[T] = GenIngredientTag(v)
  def empty[T: Taggable]: GenIngredientEmpty[T] = GenIngredientEmpty[T]()
}