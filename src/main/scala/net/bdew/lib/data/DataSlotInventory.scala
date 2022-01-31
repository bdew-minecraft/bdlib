package net.bdew.lib.data

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.inventory.BaseInventory
import net.minecraft.nbt.{CompoundTag, ListTag}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

case class DataSlotInventory(name: String, parent: DataSlotContainer, size: Int) extends DataSlot with BaseInventory {
  setUpdate(UpdateKind.SAVE, UpdateKind.UPDATE)

  override def getContainerSize: Int = size
  override def setChanged(): Unit = parent.dataSlotChanged(this)

  override def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    inv = Array.fill(size)(ItemStack.EMPTY)
    for (nbtItem <- t.getListVals[CompoundTag](name)) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.length) {
        inv(slot) = ItemStack.of(nbtItem)
      }
    }
  }

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    val itemList = new ListTag()
    for ((item, i) <- inv.view.zipWithIndex if !item.isEmpty) {
      val itemNbt: CompoundTag = new CompoundTag
      itemNbt.putByte("Slot", i.asInstanceOf[Byte])
      item.save(itemNbt)
      itemList.add(itemNbt)
    }
    t.setVal(name, itemList)
  }

  override def stillValid(player: Player): Boolean = parent.isEntityInRange(player, 64D)
}


