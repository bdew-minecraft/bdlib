package net.bdew.lib.managers

import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.client.gui.screens.{MenuScreens, Screen}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, MenuType}
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}


abstract class ContainerManager extends RegistryManager(ForgeRegistries.MENU_TYPES) {
  def registerSimple[T <: AbstractContainerMenu](id: String)(factory: (Int, Inventory, FriendlyByteBuf) => T): RegistryObject[MenuType[T]] = {
    super.register(id, () => IForgeMenuType.create((id, inv, data) => factory(id, inv, data)))
  }

  def registerPositional[E <: BlockEntity, T <: AbstractContainerMenu](id: String, te: RegistryObject[BlockEntityType[E]])(factory: (Int, Inventory, E) => T): RegistryObject[MenuType[T]] = {
    registerSimple(id) { (id, inv, data) =>
      factory(id, inv, te.get().getBlockEntity(inv.player.level, data.readBlockPos()))
    }
  }

  def registerScreen[C <: AbstractContainerMenu, S <: Screen with MenuAccess[C]](container: RegistryObject[MenuType[C]])(factory: (C, Inventory, Component) => S): Unit = {
    container.ifPresent(cc => MenuScreens.register(cc, (c: C, i, t) => factory(c, i, t)))
  }

  @OnlyIn(Dist.CLIENT) def onClientSetup(event: FMLClientSetupEvent): Unit

  override def init(): Unit = {
    super.init()
    FMLJavaModLoadingContext.get().getModEventBus.addListener(onClientSetup)
  }
}
