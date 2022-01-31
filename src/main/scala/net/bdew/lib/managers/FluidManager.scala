package net.bdew.lib.managers

import net.minecraft.world.item.{BucketItem, Items}
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.fluids.{FluidAttributes, ForgeFlowingFluid}
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

class FluidManager(blocks: BlockManager, items: ItemManager) extends RegistryManager(ForgeRegistries.FLUIDS) {
  def props(material: Material): BlockBehaviour.Properties = BlockBehaviour.Properties.of(material)

  case class FluidDef[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing](source: RegistryObject[S], flowing: RegistryObject[F])

  case class FluidDefBlock[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: LiquidBlock, BU <: BucketItem](source: RegistryObject[S], flowing: RegistryObject[F], block: RegistryObject[BL], bucket: RegistryObject[BU])

  def define[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: LiquidBlock](
                                                                                                id: String,
                                                                                                attrs: FluidAttributes.Builder,
                                                                                                sourceFactory: ForgeFlowingFluid.Properties => S,
                                                                                                flowingFactory: ForgeFlowingFluid.Properties => F,
                                                                                                blockFactory: (() => ForgeFlowingFluid) => BL,
                                                                                              ): FluidDefBlock[S, F, BL, BucketItem] = {
    var props: ForgeFlowingFluid.Properties = null
    val still = register(id, () => sourceFactory(props))
    val flowing = register(id + "_flowing", () => flowingFactory(props))
    val block = blocks.register(id, () => blockFactory(still.get))
    val bucket = items.register(id + "_bucket", () => new BucketItem(still,
      items.props.stacksTo(1).craftRemainder(Items.BUCKET)))
    props = new ForgeFlowingFluid.Properties(still, flowing, attrs)
    props.block(block).bucket(bucket)
    FluidDefBlock(still, flowing, block, bucket)
  }

  def define[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing](
                                                                             id: String,
                                                                             attrs: FluidAttributes.Builder,
                                                                             sourceFactory: ForgeFlowingFluid.Properties => S,
                                                                             flowingFactory: ForgeFlowingFluid.Properties => F,
                                                                           ): FluidDef[S, F] = {
    var props: ForgeFlowingFluid.Properties = null
    val still = register(id, () => sourceFactory(props))
    val flowing = register(id + "_flowing", () => flowingFactory(props))
    props = new ForgeFlowingFluid.Properties(still, flowing, attrs)
    FluidDef(still, flowing)
  }
}