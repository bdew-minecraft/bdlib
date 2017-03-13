/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}

/**
  * Mix in for defining capabilities in a nicer, type safe way
  */
trait CapabilityProvider extends ICapabilityProvider {
  private[capabilities] var caps = Map.empty[Capability[_], PartialFunction[EnumFacing, _]].withDefaultValue(PartialFunction.empty)

  /**
    * Add capability (using Partial Function)
    * This form is more efficient if the match is static (no if's)
    *
    * Usage: addCapability(SomeCap) { case EnumFacing.UP => myImplementation }
    */
  def addCapability[T](cap: Capability[T])(f: PartialFunction[EnumFacing, T]): Unit =
    caps += (cap -> f.orElse(caps(cap)))

  /**
    * Add capability to all faces
    */
  def addCapability[T](cap: Capability[T], handler: T): Unit =
    caps += (cap -> { case _ => handler })

  /**
    * Add capability (using Options)
    *
    * Usage: addCapabilityOption(SomeCap) { face => if (face==EnumFacing.UP) Some(myImplementation) else None }
    */
  def addCapabilityOption[T](cap: Capability[T])(f: (EnumFacing) => Option[T]): Unit =
    caps += (cap -> Function.unlift(f).orElse(caps(cap)))

  /**
    * Add a face aware capability that can be cached
    */
  def addCachedSidedCapability[T](cap: Capability[T], factory: EnumFacing => T): Unit =
    caps += (cap -> new CachedSidedCapability(factory))

  final abstract override def getCapability[T](capability: Capability[T], facing: EnumFacing): T =
    if (caps != null)
      caps(capability).applyOrElse(facing, (f: EnumFacing) => super.getCapability(capability, f)).asInstanceOf[T]
    else
      super.getCapability(capability, facing)

  final abstract override def hasCapability(capability: Capability[_], facing: EnumFacing): Boolean =
    if (caps != null)
      caps(capability).isDefinedAt(facing) || super.hasCapability(capability, facing)
    else
      super.hasCapability(capability, facing)
}
