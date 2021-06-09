package net.bdew.lib.capabilities

import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}
import net.minecraftforge.common.util.LazyOptional

class SimpleCapProvider[C](cap: Capability[C], handler: C, parent: ICapabilityProvider) extends ICapabilityProvider {
  val opt: LazyOptional[C] = LazyOptional.of(() => handler)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] =
    if (cap == this.cap)
      opt.cast()
    else
      parent.getCapability(cap, side)
}

object SimpleCapProvider {
  def apply[C](cap: Capability[C], handler: C, parent: ICapabilityProvider) = new SimpleCapProvider(cap, handler, parent)
  def apply[C](cap: Capability[C], handler: C) = new SimpleCapProvider(cap, handler, NullCapProvider)
}
