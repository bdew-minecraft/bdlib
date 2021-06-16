package net.bdew.lib.power

import net.bdew.lib.Misc
import net.bdew.lib.items.StackProperty
import net.minecraft.item.{Item, ItemStack}

trait ItemPoweredBase extends Item {
  def maxCharge: Float
  def maxReceive: Float
  def maxExtract: Float

  val charge = new StackProperty[Float]("charge")

  def setCharge(stack: ItemStack, amount: Float): Unit =
    charge.set(stack, amount)

  def getCharge(stack: ItemStack): Float =
    Misc.clamp(charge.get(stack, 0), 0f, maxCharge)

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
