package net.bdew.lib.managers

import net.bdew.lib.block.HasTE
import net.minecraft.core.BlockPos
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}


class BlockManager(items: ItemManager) extends RegistryManager(ForgeRegistries.BLOCKS) {
  outer =>
  private val tileEntities = new TEManager()

  def defaultItemProps: Item.Properties = items.props

  def props: BlockBehaviour.Properties = BlockBehaviour.Properties.of()

  def simple(id: String, props: BlockBehaviour.Properties): RegistryObject[Block] = {
    val block = register(id, () => new Block(props))
    items.register(id, () => new BlockItem(block.get, defaultItemProps))
    block
  }

  class DefBuilder[B <: Block](name: String, factory: () => B) {
    def withTE[E <: BlockEntity](teFactory: (BlockEntityType[_], BlockPos, BlockState) => E)(implicit q: B <:< B with HasTE[E]): DefBuilder_TE[B, E] =
      new DefBuilder_TE[B, E](name, q.substituteCo(factory), teFactory)
    def withItem[I <: BlockItem](biFactory: B => I): DefBuilder_BI[B, I] =
      new DefBuilder_BI(name, factory, biFactory): DefBuilder_BI[B, I]
    def withDefaultItem: DefBuilder_BI[B, BlockItem] =
      new DefBuilder_BI(name, factory, b => new BlockItem(b, defaultItemProps))
    def register: RegistryObject[B] =
      outer.register(name, factory)
  }

  class DefTE[B <: Block, E <: BlockEntity](val block: RegistryObject[B with HasTE[E]], val teType: RegistryObject[BlockEntityType[E]])

  class DefBuilder_TE[B <: Block, E <: BlockEntity](name: String, factory: () => B with HasTE[E], teFactory: (BlockEntityType[_], BlockPos, BlockState) => E) {
    def withItem[I <: BlockItem](biFactory: B => I): DefBuilder_TE_BI[B, E, I] =
      new DefBuilder_TE_BI(name, factory, teFactory, biFactory)
    def withDefaultItem: DefBuilder_TE_BI[B, E, BlockItem] =
      new DefBuilder_TE_BI(name, factory, teFactory, b => new BlockItem(b, defaultItemProps))
    def register: DefTE[B, E] = {
      val b: RegistryObject[B with HasTE[E]] = outer.register(name, factory)
      val e: RegistryObject[BlockEntityType[E]] = tileEntities.registerWithBlock(name, teFactory, b)
      new DefTE(b, e)
    }
  }

  class DefBI[B <: Block, I <: BlockItem](val block: RegistryObject[B], val item: RegistryObject[I])

  class DefBuilder_BI[B <: Block, I <: BlockItem](name: String, factory: () => B, biFactory: B => I) {
    def withTE[E <: BlockEntity](teFactory: (BlockEntityType[_], BlockPos, BlockState) => E)(implicit q: B <:< B with HasTE[E]): DefBuilder_TE_BI[B, E, I] =
      new DefBuilder_TE_BI(name, q.substituteCo(factory), teFactory, biFactory)
    def register: DefBI[B, I] = {
      val b: RegistryObject[B] = outer.register(name, factory)
      val i: RegistryObject[I] = items.register(name, () => biFactory(b.get()))
      new DefBI(b, i)
    }
  }

  class Def[B <: Block, E <: BlockEntity, I <: BlockItem](val block: RegistryObject[B with HasTE[E]], val teType: RegistryObject[BlockEntityType[E]], val item: RegistryObject[I])

  class DefBuilder_TE_BI[B <: Block, E <: BlockEntity, I <: BlockItem](name: String, factory: () => B with HasTE[E], teFactory: (BlockEntityType[_], BlockPos, BlockState) => E, biFactory: B => I) {
    def registerEx[R](f: (RegistryObject[B with HasTE[E]], RegistryObject[BlockEntityType[E]], RegistryObject[I]) => R): R = {
      val b: RegistryObject[B with HasTE[E]] = outer.register(name, factory)
      val e: RegistryObject[BlockEntityType[E]] = tileEntities.registerWithBlock(name, teFactory, b)
      val i: RegistryObject[I] = items.register(name, () => biFactory(b.get()))
      f(b, e, i)
    }

    def register: Def[B, E, I] = registerEx(new Def(_, _, _))
  }

  def define[B <: Block](name: String, factory: () => B) = new DefBuilder(name, factory)

  override def init(): Unit = {
    super.init()
    tileEntities.init()
  }
}
