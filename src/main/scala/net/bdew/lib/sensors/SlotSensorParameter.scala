package net.bdew.lib.sensors

import net.bdew.lib.container.SlotClickable
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.{ClickType, Slot}
import net.minecraft.world.item.ItemStack

class SlotSensorParameter[T, R](inv: Container, index: Int, x: Int, y: Int, ds: DataSlotSensor[T, R], obj: => Option[T]) extends Slot(inv, index, x, y) with SlotClickable {
  def onClick(clickType: ClickType, button: Int, player: Player): ItemStack = {
    obj foreach { x =>
      val newParam = ds.sensor.paramClicked(ds.param, player.containerMenu.getCarried, clickType, button, x)
      ds := ds.value.copy(param = newParam)
    }
    player.containerMenu.getCarried
  }
}
