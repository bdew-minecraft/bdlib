package net.bdew.lib.sensors

import net.bdew.lib.Misc
import net.bdew.lib.container.SlotClickable
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.{ClickType, Slot}
import net.minecraft.world.item.ItemStack

class SlotSensorType[T, R](inv: Container, index: Int, x: Int, y: Int, ds: DataSlotSensor[T, R], types: => Seq[GenericSensorType[T, R]]) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: Player): ItemStack = {
    if (clickType == ClickType.PICKUP && (button == 0 || button == 1) && types.nonEmpty && player.containerMenu.getCarried.isEmpty) {
      val newSensor =
        if (button == 0)
          Misc.nextInSeq(types, ds.sensor)
        else
          Misc.prevInSeq(types, ds.sensor)
      ds := SensorPair(newSensor, newSensor.defaultParameter)
    }
    player.containerMenu.getCarried
  }
}
