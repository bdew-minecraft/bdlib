package net.bdew.lib.managers

import net.bdew.lib.Misc
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{CreativeModeTab, ItemStack}
import net.minecraft.world.level.ItemLike
import net.minecraftforge.event.CreativeModeTabEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.RegistryObject

class CreativeTabsManager {
  private val modId = Misc.getActiveModId
  private var addedTabs = List.empty[TabEntry]
  private var changeTabs = List.empty[TabChange]

  case class TabEntry(id: String, name: Component, iconItem: () => ItemLike, items: () => Iterable[ItemLike]) {
    var tab: CreativeModeTab = _
  }

  case class TabChange(tab: CreativeModeTab, items: () => Iterable[ItemLike])

  def registerTab(id: String, name: Component, iconItem: => RegistryObject[_ <: ItemLike], items: => Iterable[RegistryObject[_ <: ItemLike]]): TabEntry = {
    val entry = TabEntry(id, name, () => iconItem.get(), () => items.map(x => x.get()))
    addedTabs :+= entry
    entry
  }

  def addToTab(tab: CreativeModeTab, items: => Iterable[RegistryObject[_ <: ItemLike]]): Unit = {
    changeTabs :+= TabChange(tab, () => items.map(x => x.get()))
  }

  def onCreativeTabBuildContents(event: CreativeModeTabEvent.BuildContents): Unit = {
    for (entry <- changeTabs if entry.tab == event.getTab; item <- entry.items()) {
      event.accept(new ItemStack(item))
    }
  }

  def onCreativeTabRegister(event: CreativeModeTabEvent.Register): Unit = {
    addedTabs.foreach(entry => {
      event.registerCreativeModeTab(new ResourceLocation(modId, entry.id), (builder: CreativeModeTab.Builder) =>
        entry.tab = builder
          .title(entry.name)
          .icon(() => new ItemStack(entry.iconItem()))
          .displayItems((enabledFlags, populator, hasPermissions) => {
            entry.items().foreach(i => populator.accept(new ItemStack(i)))
          })
          .build()
      )
    })
  }

  def init(): Unit = {
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCreativeTabRegister)
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCreativeTabBuildContents)
  }
}
