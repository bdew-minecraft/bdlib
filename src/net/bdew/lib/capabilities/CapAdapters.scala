/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities

import net.bdew.lib.capabilities.adapters.InventoryAdapter
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidHandlerItem}
import net.minecraftforge.items.IItemHandler

abstract class CapAdapter[T] {
  def canWrap(tile: TileEntity, side: EnumFacing): Boolean = false
  def canWrap(stack: ItemStack): Boolean = false
  def wrap(tile: TileEntity, side: EnumFacing): Option[T] = None
  def wrap(stack: ItemStack): Option[T] = None
}

object CapAdapters {

  class Adapters[T] {
    var adapters = List.empty[CapAdapter[T]]

    def add(adapter: CapAdapter[T]) = adapters :+= adapter

    def canWrap(tile: TileEntity, side: EnumFacing): Boolean = adapters.exists(_.canWrap(tile, side))

    def canWrap(stack: ItemStack): Boolean = adapters.exists(_.canWrap(stack))

    def wrap(tile: TileEntity, side: EnumFacing): Option[T] =
      adapters.find(_.canWrap(tile, side)).flatMap(_.wrap(tile, side))

    def wrap(stack: ItemStack): Option[T] =
      adapters.find(_.canWrap(stack)).flatMap(_.wrap(stack))
  }

  var registry = Map.empty[Capability[_], Adapters[_]]

  def add[T](cap: Capability[T], adapter: CapAdapter[T]): Unit = {
    if (!registry.isDefinedAt(cap)) registry += cap -> new Adapters[T]
    get(cap).add(adapter)
  }

  def get[T](cap: Capability[T]): Adapters[T] = registry(cap).asInstanceOf[Adapters[T]]

  def init(): Unit = {
    registry += Capabilities.CAP_FLUID_HANDLER -> new Adapters[IFluidHandler]
    registry += Capabilities.CAP_FLUID_HANDLER_ITEM -> new Adapters[IFluidHandlerItem]
    registry += Capabilities.CAP_ITEM_HANDLER -> new Adapters[IItemHandler]
    add(Capabilities.CAP_ITEM_HANDLER, InventoryAdapter)
  }
}
