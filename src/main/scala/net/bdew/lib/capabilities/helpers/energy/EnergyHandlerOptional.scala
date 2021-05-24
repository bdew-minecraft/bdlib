package net.bdew.lib.capabilities.helpers.energy

import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage

class EnergyHandlerOptional(handler: () => Option[IEnergyStorage]) extends EnergyHandlerProxy {
  override def base: IEnergyStorage = handler().getOrElse(EnergyHandlerNull)
}

object EnergyHandlerOptional {
  def create(base: () => Option[IEnergyStorage]): LazyOptional[IEnergyStorage] = {
    val handler = new EnergyHandlerOptional(base)
    LazyOptional.of(() => handler)
  }
}
