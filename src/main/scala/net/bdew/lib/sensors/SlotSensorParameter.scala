package net.bdew.lib.sensors

import net.bdew.lib.container.SlotClickable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.{ClickType, Slot}
import net.minecraft.item.ItemStack

class SlotSensorParameter[T, R](inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotSensor[T, R], obj: => Option[T]) extends Slot(inv, index, x, y) with SlotClickable {
  def onClick(clickType: ClickType, button: Int, player: PlayerEntity): ItemStack = {
    obj foreach { x =>
      val newParam = ds.sensor.paramClicked(ds.param, player.inventory.getCarried, clickType, button, x)
      ds := ds.value.copy(param = newParam)
    }
    player.inventory.getCarried
  }
}
