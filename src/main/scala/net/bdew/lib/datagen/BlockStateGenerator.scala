package net.bdew.lib.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{AirItem, BlockItem}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.generators.ModelFile.{ExistingModelFile, UncheckedModelFile}
import net.minecraftforge.client.model.generators.{BlockStateProvider, ConfiguredModel, ModelFile, VariantBlockStateBuilder}
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries

abstract class BlockStateGenerator(gen: DataGenerator, modId: String, efh: ExistingFileHelper) extends BlockStateProvider(gen, modId, efh) {
  def vanillaModel(name: String): ExistingModelFile = {
    new ExistingModelFile(new ResourceLocation(name), efh)
  }

  def uncheckedModel(name: String): ModelFile = {
    new UncheckedModelFile(new ResourceLocation(modId, name))
  }

  def genStates(block: Block, stateProvider: BlockState => ModelFile): VariantBlockStateBuilder = {
    getVariantBuilder(block)
      .forAllStates(state => {
        ConfiguredModel.builder().modelFile(stateProvider(state)).build()
      })
  }

  def blockItemModel(blockItem: BlockItem, blockModel: ModelFile): Unit = {
    itemModels()
      .getBuilder(ForgeRegistries.ITEMS.getKey(blockItem).getPath)
      .parent(blockModel)
  }

  def makeBlock(block: Block): Unit = {
    val model = uncheckedModel("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath)
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  def makeBlockItem(block: Block, model: ModelFile): Unit = {
    if (!block.asItem().isInstanceOf[AirItem])
      blockItemModel(block.asItem().asInstanceOf[BlockItem], model)
  }
}
