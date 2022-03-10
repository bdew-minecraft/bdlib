package net.bdew.lib.managers

import net.bdew.lib.Event0
import net.minecraft.world.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent

object VanillaRegister extends Event0 {
  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addGenericListener(classOf[Item], onItemsRegister)
  }

  def onItemsRegister(ev: RegistryEvent.Register[Item]): Unit = {
    trigger()
  }
}
