package net.bdew.lib.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class MultipleInventoryAdapter(inventories: List[IInventory]) extends IInventory {
  def mapInvs: List[(IInventory, Int)] =
    inventories.flatMap(x => (0 until x.getContainerSize).map(t => (x, t)))

  override def setChanged(): Unit = inventories.foreach(_.setChanged())
  override def clearContent(): Unit = inventories.foreach(_.clearContent())

  override def stillValid(player: PlayerEntity): Boolean = inventories.exists(_.stillValid(player))

  override def getContainerSize: Int = inventories.map(x => x.getContainerSize).sum
  override def isEmpty: Boolean = inventories.forall(_.isEmpty)

  private def run[T](slot: Int, f: (IInventory, Int) => T): Option[T] =
    mapInvs.lift(slot).map(x => f.tupled(x))

  override def getItem(slot: Int): ItemStack =
    run(slot, _.getItem(_)).getOrElse(ItemStack.EMPTY)

  override def removeItem(slot: Int, count: Int): ItemStack =
    run(slot, _.removeItem(_, count)).getOrElse(ItemStack.EMPTY)

  override def removeItemNoUpdate(slot: Int): ItemStack =
    run(slot, _.removeItemNoUpdate(_)).getOrElse(ItemStack.EMPTY)

  override def setItem(slot: Int, stack: ItemStack): Unit =
    run(slot, _.setItem(_, stack))
}

class MultipleInventoryAdapterStatic(inventories: List[IInventory]) extends MultipleInventoryAdapter(inventories) {
  // Optimized version for inventories that never change sizes
  override val mapInvs: List[(IInventory, Int)] = super.mapInvs
}