package net.bdew.lib.managers

import net.minecraft.world.item.{CreativeModeTab, Item}
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}


class ItemManager(itemGroup: CreativeModeTab) extends RegistryManager(ForgeRegistries.ITEMS) {
  def props: Item.Properties = new Item.Properties().tab(itemGroup)

  def simple(id: String, props: Item.Properties): RegistryObject[Item] = {
    register(id, () => new Item(props))
  }

  override def init(): Unit = {
    super.init()
  }
}
