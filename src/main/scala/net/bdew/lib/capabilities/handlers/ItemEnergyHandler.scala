package net.bdew.lib.capabilities.handlers

import net.bdew.lib.power.ItemPoweredBase
import net.minecraft.world.item.ItemStack
import net.minecraftforge.energy.IEnergyStorage

class ItemEnergyHandler(item: ItemPoweredBase, stack: ItemStack, receive: Boolean, extract: Boolean) extends IEnergyStorage {
  override def getEnergyStored: Int = item.getCharge(stack).floor.toInt
  override def getMaxEnergyStored: Int = item.maxCharge.floor.toInt

  override def canReceive: Boolean = receive
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    if (receive)
      item.injectCharge(stack, maxReceive.toFloat, simulate).floor.toInt
    else
      0
  }

  override def canExtract: Boolean = extract
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int =
    if (receive)
      item.extractCharge(stack, maxExtract.toFloat, simulate).floor.toInt
    else
      0
}
