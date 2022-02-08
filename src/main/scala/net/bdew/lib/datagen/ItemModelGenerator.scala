package net.bdew.lib.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ModelFile.{ExistingModelFile, UncheckedModelFile}
import net.minecraftforge.client.model.generators.{ItemModelProvider, ModelFile}
import net.minecraftforge.common.data.ExistingFileHelper

abstract class ItemModelGenerator(gen: DataGenerator, modId: String, efh: ExistingFileHelper) extends ItemModelProvider(gen, modId, efh) {
  def vanillaModel(name: String): ExistingModelFile = {
    new ExistingModelFile(new ResourceLocation(name), efh)
  }

  def uncheckedModel(name: String): ModelFile = {
    new UncheckedModelFile(new ResourceLocation(modId, name))
  }

  def simpleItemModel(item: Item, texture: String): Unit = {
    getBuilder(item.getRegistryName.getPath)
      .parent(vanillaModel("item/generated"))
      .texture("layer0", new ResourceLocation(modId, texture))
  }
}
