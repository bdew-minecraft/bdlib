/*
 * Copyright (c) bdew, 2013 - 2014
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
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object ItemUtils {
  def throwItemAt(world: World, x: Int, y: Int, z: Int, stack: ItemStack) {
    if ((stack == null) || world.isRemote) return
    val dx = world.rand.nextFloat * 0.8
    val dy = world.rand.nextFloat * 0.8
    val dz = world.rand.nextFloat * 0.8
    val entity = new EntityItem(world, x + dx, y + dy, z + dz, stack)
    entity.motionX = world.rand.nextGaussian * 0.05
    entity.motionY = world.rand.nextGaussian * 0.05 + 0.2
    entity.motionZ = world.rand.nextGaussian * 0.05
    world.spawnEntityInWorld(entity)
  }

  def dropItemToPlayer(world: World, player: EntityPlayer, stack: ItemStack) {
    if ((stack == null) || world.isRemote) return
    world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, stack))
  }

  def isSameItem(stack1: ItemStack, stack2: ItemStack): Boolean = {
    if (stack1 == null || stack2 == null)
      return stack1 == stack2
    if (stack1.getItem != stack2.getItem)
      return false
    if (stack1.getHasSubtypes && stack2.getItemDamage != stack1.getItemDamage)
      return false
    return ItemStack.areItemStackTagsEqual(stack2, stack1)
  }

  def addStackToSlots(stack: ItemStack, inv: IInventory, slots: Iterable[Int], checkValid: Boolean): ItemStack = {
    if (stack == null) return null

    // Try merging into existing slots
    for (slot <- slots if (!checkValid || inv.isItemValidForSlot(slot, stack)) && isSameItem(stack, inv.getStackInSlot(slot))) {
      val target = inv.getStackInSlot(slot)
      val toAdd = Misc.min(target.getMaxStackSize - target.stackSize, inv.getInventoryStackLimit - target.stackSize, stack.stackSize)
      if (toAdd >= stack.stackSize) {
        target.stackSize += stack.stackSize
        stack.stackSize = 0
        inv.markDirty()
        return null
      } else if (toAdd > 0) {
        target.stackSize += toAdd
        stack.stackSize -= toAdd
        inv.markDirty()
      }
    }

    // Now find empty slots and stick any leftovers there
    for (slot <- slots if (!checkValid || inv.isItemValidForSlot(slot, stack)) && inv.getStackInSlot(slot) == null) {
      if (inv.getInventoryStackLimit < stack.stackSize) {
        inv.setInventorySlotContents(slot, stack.splitStack(inv.getInventoryStackLimit))
      } else {
        inv.setInventorySlotContents(slot, stack.copy())
        stack.stackSize = 0
        return null
      }
    }

    return stack
  }

  def getAccessibleSlotsFromSide(inv: IInventory, side: ForgeDirection) =
    (Misc.asInstanceOpt(inv, classOf[ISidedInventory])
      map (_.getAccessibleSlotsFromSide(side.ordinal()).toList)
      getOrElse (0 until inv.getSizeInventory))

  def findItemInInventory(inv: IInventory, item: Item) =
    Range(0, inv.getSizeInventory).map(x => x -> inv.getStackInSlot(x))
      .find({ case (slot, stack) => stack != null && stack.getItem == item })
      .map({ case (slot, stack) => slot })

  def copyWithRandomSize(template: ItemStack, max: Int, rand: Random) = {
    val newSize = rand.nextInt(max)
    val newStack = template.copy()
    newStack.stackSize =
      if (newSize > newStack.getMaxStackSize)
        newStack.getMaxStackSize
      else if (newSize <= 0)
        1
      else
        newSize
    newStack
  }
}