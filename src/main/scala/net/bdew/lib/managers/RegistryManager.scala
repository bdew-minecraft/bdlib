package net.bdew.lib.managers

import net.bdew.lib.Misc
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, IForgeRegistry, RegistryObject}

class RegistryManager[T](registry: DeferredRegister[T]) {
  def this(forgeRegistry: IForgeRegistry[T]) = this(DeferredRegister.create(forgeRegistry, Misc.getActiveModId))
  def this(registryKey: ResourceKey[Registry[T]]) = this(DeferredRegister.create(registryKey, Misc.getActiveModId))

  var all = Set.empty[RegistryObject[_ <: T]]

  def init(): Unit = {
    registry.register(FMLJavaModLoadingContext.get.getModEventBus)
  }

  def register[R <: T](id: String, supplier: () => R): RegistryObject[R] = {
    val tmp: RegistryObject[R] = registry.register(id, () => supplier())
    all = all + tmp
    tmp
  }
}
