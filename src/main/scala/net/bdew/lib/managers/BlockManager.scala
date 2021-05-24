package net.bdew.lib.managers

import net.bdew.lib.block.HasTE
import net.minecraft.block.material.Material
import net.minecraft.block.{AbstractBlock, Block}
import net.minecraft.item.{BlockItem, Item}
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistries


class BlockManager(items: ItemManager) extends RegistryManager(ForgeRegistries.BLOCKS) {
  outer =>
  private val tileEntities = new TEManager()

  def defaultItemProps: Item.Properties = items.props

  def props(material: Material): AbstractBlock.Properties = AbstractBlock.Properties.of(material)

  def simple(id: String, props: AbstractBlock.Properties): RegistryObject[Block] = {
    val block = register(id, () => new Block(props))
    items.register(id, () => new BlockItem(block.get, defaultItemProps))
    block
  }

  class DefBuilder[B <: Block](name: String, factory: () => B) {
    def withTE[E <: TileEntity](teFactory: TileEntityType[_] => E)(implicit q: B <:< B with HasTE[E]): DefBuilder_TE[B, E] =
      new DefBuilder_TE[B, E](name, q.substituteCo(factory), teFactory)
    def withItem[I <: BlockItem](biFactory: B => I): DefBuilder_BI[B, I] =
      new DefBuilder_BI(name, factory, biFactory): DefBuilder_BI[B, I]
    def withDefaultItem: DefBuilder_BI[B, BlockItem] =
      new DefBuilder_BI(name, factory, b => new BlockItem(b, defaultItemProps))
    def register: RegistryObject[B] =
      outer.register(name, factory)
  }

  class DefTE[B <: Block, E <: TileEntity](val block: RegistryObject[B with HasTE[E]], val teType: RegistryObject[TileEntityType[E]])

  class DefBuilder_TE[B <: Block, E <: TileEntity](name: String, factory: () => B with HasTE[E], teFactory: TileEntityType[_] => E) {
    def withItem[I <: BlockItem](biFactory: B => I): DefBuilder_TE_BI[B, E, I] =
      new DefBuilder_TE_BI(name, factory, teFactory, biFactory)
    def withDefaultItem: DefBuilder_TE_BI[B, E, BlockItem] =
      new DefBuilder_TE_BI(name, factory, teFactory, b => new BlockItem(b, defaultItemProps))
    def register: DefTE[B, E] = {
      val b: RegistryObject[B with HasTE[E]] = outer.register(name, factory)
      val e: RegistryObject[TileEntityType[E]] = tileEntities.registerWithBlock(name, teFactory, b)
      new DefTE(b, e)
    }
  }

  class DefBI[B <: Block, I <: BlockItem](val block: RegistryObject[B], val item: RegistryObject[I])

  class DefBuilder_BI[B <: Block, I <: BlockItem](name: String, factory: () => B, biFactory: B => I) {
    def withTE[E <: TileEntity](teFactory: TileEntityType[_] => E)(implicit q: B <:< B with HasTE[E]): DefBuilder_TE_BI[B, E, I] =
      new DefBuilder_TE_BI(name, q.substituteCo(factory), teFactory, biFactory)
    def register: DefBI[B, I] = {
      val b: RegistryObject[B] = outer.register(name, factory)
      val i: RegistryObject[I] = items.register(name, () => biFactory(b.get()))
      new DefBI(b, i)
    }
  }

  class Def[B <: Block, E <: TileEntity, I <: BlockItem](val block: RegistryObject[B with HasTE[E]], val teType: RegistryObject[TileEntityType[E]], val item: RegistryObject[I])

  class DefBuilder_TE_BI[B <: Block, E <: TileEntity, I <: BlockItem](name: String, factory: () => B with HasTE[E], teFactory: TileEntityType[_] => E, biFactory: B => I) {
    def registerEx[R](f: (RegistryObject[B with HasTE[E]], RegistryObject[TileEntityType[E]], RegistryObject[I]) => R): R = {
      val b: RegistryObject[B with HasTE[E]] = outer.register(name, factory)
      val e: RegistryObject[TileEntityType[E]] = tileEntities.registerWithBlock(name, teFactory, b)
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
