package net.bdew.lib.items

import net.bdew.lib.Misc
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler

import java.util.Random

object ItemUtils {
  def throwItemAt(world: World, pos: BlockPos, stack: ItemStack): Unit = {
    if (stack.isEmpty || world.isClientSide) return
    val dx = world.random.nextFloat * 0.8
    val dy = world.random.nextFloat * 0.8
    val dz = world.random.nextFloat * 0.8
    val entity = new ItemEntity(world, pos.getX + dx, pos.getY + dy, pos.getZ + dz, stack)
    entity.setDeltaMovement(world.random.nextGaussian * 0.05, world.random.nextGaussian * 0.05 + 0.2, world.random.nextGaussian * 0.05)
    world.addFreshEntity(entity)
  }

  def dropItemToPlayer(world: World, player: PlayerEntity, stack: ItemStack): Unit = {
    if (stack.isEmpty || world.isClientSide) return
    world.addFreshEntity(new ItemEntity(world, player.position.x, player.position.y, player.position.z, stack))
  }

  def isSameItem(stack1: ItemStack, stack2: ItemStack): Boolean = {
    if (stack1.isEmpty || stack2.isEmpty)
      return stack1.isEmpty == stack2.isEmpty
    if (stack1.getItem != stack2.getItem)
      return false
    if (stack1.isDamageableItem && stack2.getDamageValue != stack1.getDamageValue)
      return false
    ItemStack.isSame(stack1, stack2)
  }

  def addStackToSlots(stack: ItemStack, inv: IInventory, slots: Iterable[Int], checkValid: Boolean): ItemStack = {
    if (stack.isEmpty) return stack

    // Try merging into existing slots
    for (slot <- slots if (!checkValid || inv.canPlaceItem(slot, stack)) && isSameItem(stack, inv.getItem(slot))) {
      val target = inv.getItem(slot)
      val toAdd = Misc.min(target.getMaxStackSize - target.getCount, inv.getMaxStackSize - target.getCount, stack.getCount)
      if (toAdd >= stack.getCount) {
        target.grow(stack.getCount)
        stack.setCount(0)
        inv.setChanged()
        return ItemStack.EMPTY
      } else if (toAdd > 0) {
        target.grow(toAdd)
        stack.shrink(toAdd)
        inv.setChanged()
      }
    }

    // Now find empty slots and stick any leftovers there
    for (slot <- slots if (!checkValid || inv.canPlaceItem(slot, stack)) && inv.getItem(slot).isEmpty) {
      if (inv.getMaxStackSize < stack.getCount) {
        inv.setItem(slot, stack.split(inv.getMaxStackSize))
      } else {
        inv.setItem(slot, stack.copy())
        stack.setCount(0)
        return ItemStack.EMPTY
      }
    }

    stack
  }

  def addStackToHandler(stack: ItemStack, inv: IItemHandler, slots: Iterable[Int]): ItemStack = {
    if (stack.isEmpty) return stack

    var left = stack.copy()

    // Try merging into existing slots
    for (slot <- slots if isSameItem(stack, inv.getStackInSlot(slot))) {
      left = inv.insertItem(slot, left, false)
      if (left.isEmpty) return left
    }

    // Now find empty slots and stick any leftovers there
    for (slot <- slots if inv.getStackInSlot(slot).isEmpty) {
      left = inv.insertItem(slot, left, false)
      if (left.isEmpty) return left
    }

    left
  }

  def addStackToHandler(stack: ItemStack, inv: IItemHandler): ItemStack =
    addStackToHandler(stack, inv, 0 until inv.getSlots)

  def getAccessibleSlotsFromSide(inv: IInventory, side: Direction): Iterable[Int] =
    (Misc.asInstanceOpt(inv, classOf[ISidedInventory])
      map (_.getSlotsForFace(side).toList)
      getOrElse (0 until inv.getContainerSize))

  def findItemInInventory(inv: IInventory, item: Item): Option[Int] =
    Range(0, inv.getContainerSize).map(x => x -> inv.getItem(x))
      .find({ case (_, stack) => !stack.isEmpty && stack.getItem == item })
      .map({ case (slot, _) => slot })

  def copyWithRandomSize(template: ItemStack, max: Int, rand: Random): ItemStack = {
    val newSize = rand.nextInt(max)
    val newStack = template.copy()
    newStack.setCount(
      if (newSize > newStack.getMaxStackSize)
        newStack.getMaxStackSize
      else if (newSize <= 0)
        1
      else
        newSize
    )
    newStack
  }
}