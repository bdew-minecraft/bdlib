package net.bdew.lib.capabilities

import net.bdew.lib.BdLib
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability

abstract class CapAdapter[T] {
  def canWrap(tile: TileEntity, side: Direction): Boolean = false
  def canWrap(stack: ItemStack): Boolean = false
  def wrap(tile: TileEntity, side: Direction): Option[T] = None
  def wrap(stack: ItemStack): Option[T] = None
}

object CapAdapters {

  class Adapters[T] {
    var adapters = List.empty[CapAdapter[T]]

    def add(adapter: CapAdapter[T]): Unit = adapters :+= adapter

    def canWrap(tile: TileEntity, side: Direction): Boolean = adapters.exists(_.canWrap(tile, side))

    def canWrap(stack: ItemStack): Boolean = adapters.exists(_.canWrap(stack))

    def wrap(tile: TileEntity, side: Direction): Option[T] =
      adapters.find(_.canWrap(tile, side)).flatMap(_.wrap(tile, side))

    def wrap(stack: ItemStack): Option[T] =
      adapters.find(_.canWrap(stack)).flatMap(_.wrap(stack))
  }

  var registry = Map.empty[Capability[_], Adapters[_]]

  def add[T](cap: Capability[T], adapter: CapAdapter[T]): Unit = {
    get(cap).add(adapter)
  }

  def get[T](cap: Capability[T]): Adapters[T] = {
    if (!registry.isDefinedAt(cap)) {
      BdLib.logInfo(s"Init cap adapters for ${cap.getName}")
      registry += cap -> new Adapters[T]
    }
    registry(cap).asInstanceOf[Adapters[T]]
  }
}
