package net.bdew.lib.misc

import net.minecraft.core.registries.{BuiltInRegistries, Registries}
import net.minecraft.core.{Holder, Registry}
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistry}

import scala.annotation.implicitNotFound
import scala.jdk.CollectionConverters._
import scala.jdk.StreamConverters._

@implicitNotFound("Type ${T} is not taggable")
trait Taggable[T] {
  def resKey: ResourceKey[Registry[T]]
  def registry: IForgeRegistry[T]
  def ref(v: T): Holder.Reference[T]
  def tags(v: T): List[TagKey[T]] = ref(v).tags().toScala(List)
  def is(v: T, t: TagKey[T]): Boolean = ref(v).is(t)
  def createTag(loc: ResourceLocation): TagKey[T] = TagKey.create(resKey, loc)
  def resolve(t: TagKey[T]): Set[T]
  def tagMap: Map[TagKey[T], List[T]] = {
    registry.getValues.asScala.toList
      .flatMap(o => tags(o).map(t => t -> o))
      .groupBy(_._1).map(x => x._1 -> x._2.map(_._2))
  }
}

object Taggable {
  def apply[T: Taggable]: Taggable[T] = implicitly[Taggable[T]]

  implicit val TagableItem: Taggable[Item] = new Taggable[Item] {
    override def resKey: ResourceKey[Registry[Item]] = Registries.ITEM
    override def registry: IForgeRegistry[Item] = ForgeRegistries.ITEMS
    override def ref(v: Item): Holder.Reference[Item] = v.builtInRegistryHolder()
    override def resolve(t: TagKey[Item]): Set[Item] = BuiltInRegistries.ITEM.getTagOrEmpty(t).asScala.map(_.value()).toSet
  }

  implicit val TagableBlock: Taggable[Block] = new Taggable[Block] {
    override def resKey: ResourceKey[Registry[Block]] = Registries.BLOCK
    override def registry: IForgeRegistry[Block] = ForgeRegistries.BLOCKS
    override def ref(v: Block): Holder.Reference[Block] = v.builtInRegistryHolder()
    override def resolve(t: TagKey[Block]): Set[Block] = BuiltInRegistries.BLOCK.getTagOrEmpty(t).asScala.map(_.value()).toSet
  }

  implicit val TagableFluid: Taggable[Fluid] = new Taggable[Fluid] {
    override def resKey: ResourceKey[Registry[Fluid]] = Registries.FLUID
    override def registry: IForgeRegistry[Fluid] = ForgeRegistries.FLUIDS
    override def ref(v: Fluid): Holder.Reference[Fluid] = v.builtInRegistryHolder()
    override def resolve(t: TagKey[Fluid]): Set[Fluid] = BuiltInRegistries.FLUID.getTagOrEmpty(t).asScala.map(_.value()).toSet
  }
}