package net.bdew.lib.managers

import net.bdew.lib.Misc
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

class VanillaRegistryObject[R](registry: Registry[_ >: R], id: ResourceLocation, factory: () => R) {
  private var instance: R = _

  def get: R = instance

  def register(): Unit = {
    instance = Registry.register(registry, id, factory())
  }
}

class VanillaRegistryManager[T](registry: Registry[T]) {
  val modId: String = Misc.getActiveModId
  var all = Set.empty[VanillaRegistryObject[_ <: T]]

  def register[R <: T](id: String, factory: () => R): VanillaRegistryObject[R] = {
    val tmp = new VanillaRegistryObject(registry, new ResourceLocation(modId, id), factory)
    all = all + tmp
    tmp
  }

  def init(): Unit = {
    FMLJavaModLoadingContext.get.getModEventBus.addGenericListener(classOf[Item], doRegistration)
  }

  private def doRegistration(ev: RegistryEvent.Register[Item]): Unit = {
    all.foreach(_.register())
  }
}
