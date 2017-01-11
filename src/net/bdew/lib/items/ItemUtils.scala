/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items

import java.util.Random

import net.bdew.lib.Misc
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler

object ItemUtils {
  def throwItemAt(world: World, pos: BlockPos, stack: ItemStack) {
    if (stack.isEmpty || world.isRemote) return
    val dx = world.rand.nextFloat * 0.8
    val dy = world.rand.nextFloat * 0.8
    val dz = world.rand.nextFloat * 0.8
    val entity = new EntityItem(world, pos.getX + dx, pos.getY + dy, pos.getZ + dz, stack)
    entity.motionX = world.rand.nextGaussian * 0.05
    entity.motionY = world.rand.nextGaussian * 0.05 + 0.2
    entity.motionZ = world.rand.nextGaussian * 0.05
    world.spawnEntity(entity)
  }

  def dropItemToPlayer(world: World, player: EntityPlayer, stack: ItemStack) {
    if (stack.isEmpty || world.isRemote) return
    world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, stack))
  }

  def isSameItem(stack1: ItemStack, stack2: ItemStack): Boolean = {
    if (stack1.isEmpty || stack2.isEmpty)
      return stack1.isEmpty == stack2.isEmpty
    if (stack1.getItem != stack2.getItem)
      return false
    if (stack1.getHasSubtypes && stack2.getItemDamage != stack1.getItemDamage)
      return false
    return ItemStack.areItemStackTagsEqual(stack2, stack1)
  }

  def addStackToSlots(stack: ItemStack, inv: IInventory, slots: Iterable[Int], checkValid: Boolean): ItemStack = {
    if (stack.isEmpty) return stack

    // Try merging into existing slots
    for (slot <- slots if (!checkValid || inv.isItemValidForSlot(slot, stack)) && isSameItem(stack, inv.getStackInSlot(slot))) {
      val target = inv.getStackInSlot(slot)
      val toAdd = Misc.min(target.getMaxStackSize - target.getCount, inv.getInventoryStackLimit - target.getCount, stack.getCount)
      if (toAdd >= stack.getCount) {
        target.grow(stack.getCount)
        stack.setCount(0)
        inv.markDirty()
        return ItemStack.EMPTY
      } else if (toAdd > 0) {
        target.grow(toAdd)
        stack.shrink(toAdd)
        inv.markDirty()
      }
    }

    // Now find empty slots and stick any leftovers there
    for (slot <- slots if (!checkValid || inv.isItemValidForSlot(slot, stack)) && inv.getStackInSlot(slot).isEmpty) {
      if (inv.getInventoryStackLimit < stack.getCount) {
        inv.setInventorySlotContents(slot, stack.splitStack(inv.getInventoryStackLimit))
      } else {
        inv.setInventorySlotContents(slot, stack.copy())
        stack.setCount(0)
        return ItemStack.EMPTY
      }
    }

    return stack
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

    return left
  }

  def addStackToHandler(stack: ItemStack, inv: IItemHandler): ItemStack =
    addStackToHandler(stack, inv, 0 until inv.getSlots)

  def getAccessibleSlotsFromSide(inv: IInventory, side: EnumFacing) =
    (Misc.asInstanceOpt(inv, classOf[ISidedInventory])
      map (_.getSlotsForFace(side).toList)
      getOrElse (0 until inv.getSizeInventory))

  def findItemInInventory(inv: IInventory, item: Item) =
    Range(0, inv.getSizeInventory).map(x => x -> inv.getStackInSlot(x))
      .find({ case (slot, stack) => !stack.isEmpty && stack.getItem == item })
      .map({ case (slot, stack) => slot })

  def copyWithRandomSize(template: ItemStack, max: Int, rand: Random) = {
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