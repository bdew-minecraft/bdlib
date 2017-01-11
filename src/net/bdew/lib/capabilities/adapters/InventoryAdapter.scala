/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.capabilities.adapters

import net.bdew.lib.capabilities.CapAdapter
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.{InvWrapper, SidedInvWrapper}

object InventoryAdapter extends CapAdapter[IItemHandler] {
  override def canWrap(tile: TileEntity, side: EnumFacing): Boolean =
    tile.isInstanceOf[IInventory]
  override def wrap(tile: TileEntity, side: EnumFacing): Option[IItemHandler] = {
    if (tile.isInstanceOf[ISidedInventory])
      Some(new SidedInvWrapper(tile.asInstanceOf[ISidedInventory], side))
    else if (tile.isInstanceOf[IInventory])
      Some(new InvWrapper(tile.asInstanceOf[IInventory]))
    else None
  }
}
