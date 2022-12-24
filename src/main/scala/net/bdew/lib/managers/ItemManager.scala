package net.bdew.lib.managers

import net.minecraft.world.item.Item
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

class ItemManager() extends RegistryManager(ForgeRegistries.ITEMS) {
  def props: Item.Properties = new Item.Properties()

  def simple(id: String, props: Item.Properties): RegistryObject[Item] = {
    register(id, () => new Item(props))
  }

  override def init(): Unit = {
    super.init()
  }
}
