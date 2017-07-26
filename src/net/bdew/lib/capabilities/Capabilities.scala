/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.minecraftforge.common.capabilities.{Capability, CapabilityInject}
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidHandlerItem}
import net.minecraftforge.items.IItemHandler

import scala.annotation.meta.setter

object Capabilities {
  @(CapabilityInject@setter)(classOf[IFluidHandler])
  var CAP_FLUID_HANDLER: Capability[IFluidHandler] = _

  @(CapabilityInject@setter)(classOf[IFluidHandlerItem])
  var CAP_FLUID_HANDLER_ITEM: Capability[IFluidHandlerItem] = _

  @(CapabilityInject@setter)(classOf[IItemHandler])
  var CAP_ITEM_HANDLER: Capability[IItemHandler] = _
}
