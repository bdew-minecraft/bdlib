package net.bdew.lib.container

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory._
import net.minecraft.world.item.ItemStack

abstract class NoInvContainer(containerType: MenuType[_], id: Int) extends AbstractContainerMenu(containerType, id) {
  protected var players = Set.empty[ServerPlayer]

  protected def addSlotInternal(slot: Slot): Slot = super.addSlot(slot)

  protected def bindPlayerInventory(inv: Inventory, xOffs: Int, yOffsInv: Int, yOffsHotbar: Int): Unit = {
    for (i <- 0 until 3; j <- 0 until 9)
      addSlot(new Slot(inv, j + i * 9 + 9, xOffs + j * 18, yOffsInv + i * 18))

    for (i <- 0 until 9)
      addSlot(new Slot(inv, i, xOffs + i * 18, yOffsHotbar))
  }

  override def clicked(slotNum: Int, button: Int, clickType: ClickType, player: Player): Unit = {
    if (slotNum >= 0 && slotNum < slots.size) {
      val slot = getSlot(slotNum)
      if (slot != null && slot.isInstanceOf[SlotClickable]) {
        slot.asInstanceOf[SlotClickable].onClick(clickType, button, player)
        return
      }
    }
    super.clicked(slotNum, button, clickType, player)
  }

  def playerAdded(player: ServerPlayer): Unit = {
    players += player
  }

  def playerRemoved(player: ServerPlayer): Unit = {
    players -= player
  }

  override def quickMoveStack(player : Player, slot : Int): ItemStack = {
    ItemStack.EMPTY
  }
}
