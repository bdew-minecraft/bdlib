package net.bdew.lib.managers

import net.bdew.lib.Misc
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, IForgeRegistry, IForgeRegistryEntry, RegistryObject}

class RegistryManager[T <: IForgeRegistryEntry[T]](forgeRegistry: IForgeRegistry[T]) {
  val modId: String = Misc.getActiveModId
  val registry: DeferredRegister[T] = DeferredRegister.create(forgeRegistry, modId)
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
