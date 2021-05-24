/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.power

import net.bdew.lib.Misc
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT

trait ItemPoweredBase extends Item {
  def maxCharge: Float

  def getCharge(stack: ItemStack): Float = {
    if (!stack.hasTag) setCharge(stack, 0)
    Misc.clamp(stack.getTag.getFloat("charge"), 0, maxCharge)
  }

  def useCharge(stack: ItemStack, amount: Float): Boolean = {
    val charge = getCharge(stack)
    if (charge >= amount) {
      setCharge(stack, charge - amount)
      true
    } else false
  }

  def hasCharge(stack: ItemStack, amount: Float): Boolean = {
    getCharge(stack) >= amount
  }

  def setCharge(stack: ItemStack, charge: Float): Unit = {
    if (!stack.hasTag) stack.setTag(new CompoundNBT())
    stack.getTag.putFloat("charge", charge)
    updateDamage(stack)
  }

  def injectCharge(stack: ItemStack, v: Float, simulate: Boolean): Float = {
    val current = getCharge(stack)
    val canAdd = Misc.clamp(v, 0F, maxCharge - current)
    if ((canAdd > 0) && !simulate) {
      setCharge(stack, current + canAdd)
    }
    canAdd
  }

  def extractCharge(stack: ItemStack, v: Float, simulate: Boolean): Float = {
    val current = getCharge(stack)
    val canExtract = Misc.clamp(v, 0F, current)
    if ((canExtract > 0) && !simulate) {
      setCharge(stack, current - canExtract)
    }
    canExtract
  }

  def updateDamage(stack: ItemStack): Unit = {
    setDamage(stack, Misc.clamp((100 * (1 - getCharge(stack) / maxCharge)).round, 1, 100))
  }

  def stackWithCharge(charge: Float): ItemStack = {
    val n = new ItemStack(this)
    setCharge(n, charge)
    n
  }

  override def setDamage(stack: ItemStack, damage: Int): Unit = super.setDamage(stack, Misc.clamp(damage, 1, 100))
}
