package net.bdew.lib.inventory

import net.bdew.lib.items.ItemUtils
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.Level

import java.util

trait BaseInventory extends Container {
  var inv: Array[ItemStack] = Array.fill(getContainerSize)(ItemStack.EMPTY)

  override def getMaxStackSize: Int = 64

  override def stillValid(player: Player): Boolean = true
  override def startOpen(player: Player): Unit = {}
  override def stopOpen(player: Player): Unit = {}

  override def canPlaceItem(slot: Int, stack: ItemStack): Boolean = true

  override def getItem(i: Int): ItemStack = inv(i)

  override def setItem(slot: Int, stack: ItemStack): Unit = {
    require(stack != null)
    inv(slot) = stack
    setChanged()
  }

  override def removeItem(slot: Int, amount: Int): ItemStack = {
    val item = inv(slot)
    if (item.isEmpty) {
      item
    } else {
      val newStack = item.split(amount)
      setChanged()
      newStack
    }
  }

  override def removeItemNoUpdate(slot: Int): ItemStack = {
    val st = inv(slot)
    inv(slot) = ItemStack.EMPTY
    st
  }

  override def clearContent(): Unit = {
    inv = Array.fill(getContainerSize)(ItemStack.EMPTY)
    setChanged()
  }

  override def isEmpty: Boolean = inv.forall(_.isEmpty)

  override def countItem(item: Item): Int =
    inv.filter(x => x.getItem.equals(item)).map(x => x.getCount).sum

  override def hasAnyOf(items: util.Set[Item]): Boolean =
    inv.exists(x => items.contains(x.getItem))

  def dropContent(world: Level, pos: BlockPos): Unit = {
    for (stack <- inv if !stack.isEmpty) {
      ItemUtils.throwItemAt(world, pos, stack)
    }
    clearContent()
  }
}