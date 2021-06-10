package net.bdew.lib.power

import net.bdew.lib.Misc
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT

trait ItemPoweredBase extends Item {
  def maxCharge: Float
  def maxReceive: Float
  def maxExtract: Float

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
  }

  def injectCharge(stack: ItemStack, v: Float, simulate: Boolean): Float = {
    val current = getCharge(stack)
    val canAdd = Misc.clamp(v, 0F, Math.min(maxCharge - current, maxReceive))
    if ((canAdd > 0) && !simulate) {
      setCharge(stack, current + canAdd)
    }
    canAdd
  }

  def extractCharge(stack: ItemStack, v: Float, simulate: Boolean): Float = {
    val current = getCharge(stack)
    val canExtract = Misc.clamp(v, 0F, Math.min(current, maxExtract))
    if ((canExtract > 0) && !simulate) {
      setCharge(stack, current - canExtract)
    }
    canExtract
  }

  def stackWithCharge(charge: Float): ItemStack = {
    val n = new ItemStack(this)
    setCharge(n, charge)
    n
  }

  override def showDurabilityBar(stack: ItemStack) = true

  override def getDurabilityForDisplay(stack: ItemStack): Double = 1 - 1D * getCharge(stack) / maxCharge
}
