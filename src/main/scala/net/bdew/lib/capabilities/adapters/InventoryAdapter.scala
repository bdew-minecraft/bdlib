package net.bdew.lib.capabilities.adapters

import net.bdew.lib.capabilities.CapAdapter
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.{InvWrapper, SidedInvWrapper}

object InventoryAdapter extends CapAdapter[IItemHandler] {
  override def canWrap(tile: TileEntity, side: Direction): Boolean =
    tile.isInstanceOf[IInventory]
  override def wrap(tile: TileEntity, side: Direction): Option[IItemHandler] = {
    tile match {
      case inventory: ISidedInventory => Some(new SidedInvWrapper(inventory, side))
      case inventory: IInventory => Some(new InvWrapper(inventory))
      case _ => None
    }
  }
}
