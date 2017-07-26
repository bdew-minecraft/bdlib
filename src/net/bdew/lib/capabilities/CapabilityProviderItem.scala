/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}

/**
  * Mix in for defining capabilities in a nicer, type safe way
  */
trait CapabilityProviderItem extends Item {
  private[capabilities] var caps = Map.empty[Capability[_], PartialFunction[ItemStack, _]].withDefaultValue(PartialFunction.empty)

  /**
    * Add capability
    *
    * Usage: addCapability(SomeCap) { case stack => ... }
    */
  def addCapability[T](cap: Capability[T])(f: PartialFunction[ItemStack, T]): Unit =
    caps += (cap -> f.orElse(caps(cap)))

  def addCapability[T](cap: Capability[T], f: ItemStack => T): Unit = {
    caps += (cap -> PartialFunction(f).orElse(caps(cap)))
  }

  override def initCapabilities(stack: ItemStack, nbt: NBTTagCompound): ICapabilityProvider =
    new CapabilityStackProxy(stack, Option(super.initCapabilities(stack, nbt)).getOrElse(NullCapabilityProvider))

  private class CapabilityStackProxy(stack: ItemStack, prev: ICapabilityProvider) extends ICapabilityProvider {
    override final def getCapability[T](capability: Capability[T], facing: EnumFacing): T =
      caps(capability).applyOrElse(stack, (s: ItemStack) => prev.getCapability(capability, facing)).asInstanceOf[T]

    override final def hasCapability(capability: Capability[_], facing: EnumFacing): Boolean =
      caps(capability).isDefinedAt(stack) || prev.hasCapability(capability, facing)
  }

}
