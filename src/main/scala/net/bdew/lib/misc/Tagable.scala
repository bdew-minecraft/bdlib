package net.bdew.lib.misc

import net.minecraft.core.{Holder, Registry}
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.registries.{ForgeRegistries, ForgeRegistryEntry, IForgeRegistry}

import scala.annotation.implicitNotFound
import scala.jdk.CollectionConverters._
import scala.jdk.StreamConverters._

@implicitNotFound("Type ${T} is not tagable")
trait Tagable[T <: ForgeRegistryEntry[T]] {
  def resKey: ResourceKey[Registry[T]]
  def registry: IForgeRegistry[T]
  def ref(v: T): Holder.Reference[T]
  def tags(v: T): List[TagKey[T]] = ref(v).tags().toScala(List)
  def is(v: T, t: TagKey[T]): Boolean = ref(v).is(t)
  def createTag(loc: ResourceLocation): TagKey[T] = TagKey.create(resKey, loc)
  def tagMap: Map[TagKey[T], List[T]] = {
    registry.getValues.asScala.toList
      .flatMap(o => tags(o).map(t => t -> o))
      .groupBy(_._1).map(x => x._1 -> x._2.map(_._2))
  }
}

object Tagable {
  def apply[T <: ForgeRegistryEntry[T] : Tagable]: Tagable[T] = implicitly[Tagable[T]]

  implicit val TagableItem: Tagable[Item] = new Tagable[Item] {
    override def resKey: ResourceKey[Registry[Item]] = Registry.ITEM_REGISTRY
    override def registry: IForgeRegistry[Item] = ForgeRegistries.ITEMS
    override def ref(v: Item): Holder.Reference[Item] = v.builtInRegistryHolder()
  }

  implicit val TagableBlock: Tagable[Block] = new Tagable[Block] {
    override def resKey: ResourceKey[Registry[Block]] = Registry.BLOCK_REGISTRY
    override def registry: IForgeRegistry[Block] = ForgeRegistries.BLOCKS
    override def ref(v: Block): Holder.Reference[Block] = v.builtInRegistryHolder()
  }

  implicit val TagableFluid: Tagable[Fluid] = new Tagable[Fluid] {
    override def resKey: ResourceKey[Registry[Fluid]] = Registry.FLUID_REGISTRY
    override def registry: IForgeRegistry[Fluid] = ForgeRegistries.FLUIDS
    override def ref(v: Fluid): Holder.Reference[Fluid] = v.builtInRegistryHolder()
  }
}