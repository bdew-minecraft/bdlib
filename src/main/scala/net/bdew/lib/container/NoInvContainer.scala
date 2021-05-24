package net.bdew.lib.container

import net.minecraft.entity.player.{PlayerEntity, PlayerInventory, ServerPlayerEntity}
import net.minecraft.inventory.container._
import net.minecraft.item.ItemStack

abstract class NoInvContainer(containerType: ContainerType[_], id: Int) extends Container(containerType, id) {
  var players = Set.empty[ServerPlayerEntity]

  protected def bindPlayerInventory(inv: PlayerInventory, xOffs: Int, yOffsInv: Int, yOffsHotbar: Int): Unit = {
    for (i <- 0 until 3; j <- 0 until 9)
      addSlot(new Slot(inv, j + i * 9 + 9, xOffs + j * 18, yOffsInv + i * 18))

    for (i <- 0 until 9)
      addSlot(new Slot(inv, i, xOffs + i * 18, yOffsHotbar))
  }

  override def clicked(slotNum: Int, button: Int, clickType: ClickType, player: PlayerEntity): ItemStack = {
    if (slotNum >= 0 && slotNum < slots.size) {
      val slot = getSlot(slotNum)
      if (slot != null && slot.isInstanceOf[SlotClickable])
        return slot.asInstanceOf[SlotClickable].onClick(clickType, button, player)
    }
    super.clicked(slotNum, button, clickType, player)
  }

  protected def playerAdded(player: ServerPlayerEntity): Unit = {
    players += player
  }

  protected def playerRemoved(player: ServerPlayerEntity): Unit = {
    players -= player
  }

  override def addSlotListener(listener: IContainerListener): Unit = {
    super.addSlotListener(listener)
    listener match {
      case player: ServerPlayerEntity => playerAdded(player)
      case _ =>
    }
  }

  override def removeSlotListener(listener: IContainerListener): Unit = {
    super.removeSlotListener(listener)
    listener match {
      case player: ServerPlayerEntity => playerRemoved(player)
      case _ =>
    }
  }
}
