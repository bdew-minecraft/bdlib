package net.bdew.lib.managers

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.{IHasContainer, ScreenManager}
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.ForgeRegistries


abstract class ContainerManager extends RegistryManager(ForgeRegistries.CONTAINERS) {
  def registerSimple[T <: Container](id: String)(factory: (Int, PlayerInventory, PacketBuffer) => T): RegistryObject[ContainerType[T]] = {
    super.register(id, () => IForgeContainerType.create((id, inv, data) => factory(id, inv, data)))
  }

  def registerPositional[E <: TileEntity, T <: Container](id: String, te: RegistryObject[TileEntityType[E]])(factory: (Int, PlayerInventory, E) => T): RegistryObject[ContainerType[T]] = {
    registerSimple(id) { (id, inv, data) =>
      factory(id, inv, te.get().getBlockEntity(inv.player.level, data.readBlockPos()))
    }
  }

  def registerScreen[C <: Container, S <: Screen with IHasContainer[C]](container: RegistryObject[ContainerType[C]])(factory: (C, PlayerInventory, ITextComponent) => S): Unit = {
    container.ifPresent(cc => ScreenManager.register(cc, (c: C, i, t) => factory(c, i, t)))
  }

  @OnlyIn(Dist.CLIENT) def onClientSetup(event: FMLClientSetupEvent): Unit

  override def init(): Unit = {
    super.init()
    FMLJavaModLoadingContext.get().getModEventBus.addListener(onClientSetup)
  }
}
