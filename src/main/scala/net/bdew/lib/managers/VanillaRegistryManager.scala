package net.bdew.lib.managers

import net.bdew.lib.Misc
import net.minecraft.core.Registry
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, RegistryObject}

class VanillaRegistryManager[T](registry: Registry[T]) {
  val modId: String = Misc.getActiveModId
  var all = Set.empty[RegistryObject[_ <: T]]
  val deferred: DeferredRegister[T] = DeferredRegister.create(registry.key(), modId)

  def register[R <: T](id: String, supplier: () => R): RegistryObject[R] = {
    val tmp: RegistryObject[R] = deferred.register(id, () => supplier())
    all = all + tmp
    tmp
  }

  def init(): Unit = {
    deferred.register(FMLJavaModLoadingContext.get.getModEventBus)
  }
}
