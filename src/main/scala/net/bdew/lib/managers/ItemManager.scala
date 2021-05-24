package net.bdew.lib.managers

import net.minecraft.item.{Item, ItemGroup}
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistries


class ItemManager(itemGroup: ItemGroup) extends RegistryManager(ForgeRegistries.ITEMS) {
  def props: Item.Properties = new Item.Properties().tab(itemGroup)

  def simple(id: String, props: Item.Properties): RegistryObject[Item] = {
    register(id, () => new Item(props))
  }

  override def init(): Unit = {
    super.init()
  }
}
