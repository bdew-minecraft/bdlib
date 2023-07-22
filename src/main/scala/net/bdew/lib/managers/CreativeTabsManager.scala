package net.bdew.lib.managers

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.{CreativeModeTab, ItemStack}
import net.minecraft.world.level.ItemLike
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.RegistryObject

class CreativeTabsManager extends RegistryManager(Registries.CREATIVE_MODE_TAB) {
  private var changeTabs = List.empty[TabChange]

  case class TabChange(tab: ResourceKey[CreativeModeTab], items: () => Iterable[ItemLike])

  def registerTab(id: String, name: Component, iconItem: => RegistryObject[_ <: ItemLike], items: => Iterable[RegistryObject[_ <: ItemLike]]): RegistryObject[CreativeModeTab] = {
    register(id, () => CreativeModeTab.builder()
      .icon(() => new ItemStack(iconItem.get()))
      .title(name)
      .displayItems((params, output) => {
        items.foreach(i => output.accept(i.get()))
      })
      .build()
    )
  }

  def addToTab(tab: ResourceKey[CreativeModeTab], items: => Iterable[RegistryObject[_ <: ItemLike]]): Unit = {
    changeTabs :+= TabChange(tab, () => items.map(x => x.get()))
  }

  def onCreativeTabBuildContents(event: BuildCreativeModeTabContentsEvent): Unit = {
    for (entry <- changeTabs if entry.tab == event.getTabKey; item <- entry.items()) {
      event.accept(new ItemStack(item))
    }
  }

  override def init(): Unit = {
    super.init()
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCreativeTabBuildContents)
  }
}
