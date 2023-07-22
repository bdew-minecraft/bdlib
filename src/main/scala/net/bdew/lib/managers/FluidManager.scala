package net.bdew.lib.managers

import net.minecraft.world.item.{BucketItem, Items}
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.fluids.{FluidType, ForgeFlowingFluid}
import net.minecraftforge.registries.ForgeRegistries.Keys
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

class FluidManager(blocks: BlockManager, items: ItemManager) extends RegistryManager(ForgeRegistries.FLUIDS) {
  val types = new RegistryManager(Keys.FLUID_TYPES)

  def props: BlockBehaviour.Properties = BlockBehaviour.Properties.of()

  case class FluidDef[T <: FluidType, S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing](fluidType: RegistryObject[T], source: RegistryObject[S], flowing: RegistryObject[F])

  case class FluidDefBlock[T <: FluidType, S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: LiquidBlock, BU <: BucketItem](fluidType: RegistryObject[T], source: RegistryObject[S], flowing: RegistryObject[F], block: RegistryObject[BL], bucket: RegistryObject[BU])

  def define[T <: FluidType, S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: LiquidBlock](
                                                                                                                id: String,
                                                                                                                typeFactory: () => T,
                                                                                                                sourceFactory: ForgeFlowingFluid.Properties => S,
                                                                                                                flowingFactory: ForgeFlowingFluid.Properties => F,
                                                                                                                blockFactory: (() => ForgeFlowingFluid) => BL,
                                                                                                              ): FluidDefBlock[T, S, F, BL, BucketItem] = {
    var props: ForgeFlowingFluid.Properties = null
    val fluidType = types.register(id, typeFactory)
    val still = register(id, () => sourceFactory(props))
    val flowing = register(id + "_flowing", () => flowingFactory(props))
    val block = blocks.register(id, () => blockFactory(still.get))
    val bucket = items.register(id + "_bucket", () => new BucketItem(still,
      items.props.stacksTo(1).craftRemainder(Items.BUCKET)))
    props = new ForgeFlowingFluid.Properties(fluidType, still, flowing)
    props.block(block).bucket(bucket)
    FluidDefBlock(fluidType, still, flowing, block, bucket)
  }

  def define[T <: FluidType, S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing](
                                                                                             id: String,
                                                                                             typeFactory: () => T,
                                                                                             sourceFactory: ForgeFlowingFluid.Properties => S,
                                                                                             flowingFactory: ForgeFlowingFluid.Properties => F,
                                                                                           ): FluidDef[T, S, F] = {
    var props: ForgeFlowingFluid.Properties = null
    val fluidType = types.register(id, typeFactory)
    val still = register(id, () => sourceFactory(props))
    val flowing = register(id + "_flowing", () => flowingFactory(props))
    props = new ForgeFlowingFluid.Properties(fluidType, still, flowing)
    FluidDef(fluidType, still, flowing)
  }

  override def init(): Unit = {
    super.init()
    this.types.init()
  }
}