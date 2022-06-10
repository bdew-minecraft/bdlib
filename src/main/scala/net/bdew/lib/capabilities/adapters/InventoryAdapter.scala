package net.bdew.lib.capabilities.adapters

import net.bdew.lib.capabilities.CapAdapter
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.{Container, WorldlyContainer}
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.{InvWrapper, SidedInvWrapper}

object InventoryAdapter extends CapAdapter[IItemHandler] {
  override def canWrap(tile: BlockEntity, side: Direction): Boolean =
    tile.isInstanceOf[Container]
  override def wrap(tile: BlockEntity, side: Direction): Option[IItemHandler] = {
    tile match {
      case inventory: WorldlyContainer => Some(new SidedInvWrapper(inventory, side))
      case inventory: Container => Some(new InvWrapper(inventory))
      case _ => None
    }
  }
}
