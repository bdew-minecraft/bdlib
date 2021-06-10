package net.bdew.lib.inventory

import net.bdew.lib.PimpVanilla.pimpNBT
import net.bdew.lib.nbt.NBT
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompoundNBT, ListNBT}

class StackInventory(stack: ItemStack, size: Int) extends BaseInventory {
  override def getContainerSize: Int = size

  if (stack.hasTag && stack.getTag.contains("Inventory")) {
    for (nbtItem <- stack.getTag.getListVals[CompoundNBT]("Inventory")) {
      val slot = nbtItem.getByte("Slot")
      if (slot >= 0 && slot < inv.length) {
        inv(slot) = ItemStack.of(nbtItem)
      }
    }
  }

  override def setChanged(): Unit = {
    val itemList = new ListNBT()
    for ((item, i) <- inv.view.zipWithIndex if !item.isEmpty) {
      itemList.add(NBT.from(itemNbt => {
        itemNbt.putByte("Slot", i.asInstanceOf[Byte])
        item.save(itemNbt)
      }))
    }
    stack.getOrCreateTag().put("Inventory", itemList)
  }
}
