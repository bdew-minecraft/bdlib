package net.bdew.lib.capabilities

import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}
import net.minecraftforge.common.util.LazyOptional

object NullCapProvider extends ICapabilityProvider {
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = LazyOptional.empty()
}
