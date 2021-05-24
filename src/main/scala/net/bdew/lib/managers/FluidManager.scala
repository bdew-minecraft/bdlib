package net.bdew.lib.managers

import net.minecraft.block.material.Material
import net.minecraft.block.{AbstractBlock, FlowingFluidBlock}
import net.minecraft.item.BucketItem
import net.minecraftforge.fluids.{FluidAttributes, ForgeFlowingFluid}
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistries

class FluidManager(blocks: BlockManager, items: ItemManager) extends RegistryManager(ForgeRegistries.FLUIDS) {
  def props(material: Material): AbstractBlock.Properties = AbstractBlock.Properties.of(material)

  case class FluidDef[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing](source: RegistryObject[S], flowing: RegistryObject[F])

  case class FluidDefBlock[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: FlowingFluidBlock, BU <: BucketItem](source: RegistryObject[S], flowing: RegistryObject[F], block: RegistryObject[BL], bucket: RegistryObject[BU])

  def define[S <: ForgeFlowingFluid.Source, F <: ForgeFlowingFluid.Flowing, BL <: FlowingFluidBlock](
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
      items.props.stacksTo(1).craftRemainder(net.minecraft.item.Items.BUCKET)))
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