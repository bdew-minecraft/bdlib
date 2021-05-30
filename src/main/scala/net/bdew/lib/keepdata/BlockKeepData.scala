package net.bdew.lib.keepdata

import net.bdew.lib.block.HasTE
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}

/**
 * Mixin for blocks to keep their data when broken
 * Must have a TileEntity that extends TileKeepData
 */
trait BlockKeepData extends Block {
  this: HasTE[_ <: TileKeepData] =>

  override def getCloneItemStack(world: IBlockReader, pos: BlockPos, state: BlockState): ItemStack = {
    val stack = super.getCloneItemStack(world, pos, state)
    getTE(world, pos).foreach(_.saveToItem(stack))
    stack
  }

  /**
   * Override for special actions on placing
   * ItemStack might or might not contain actual data
   * Called with the TE created and added to world, after onBlockPlacedBy & co
   */
  def restoreTileEntity(world: World, pos: BlockPos, is: ItemStack, player: PlayerEntity) =
    getTE(world, pos).loadFromItem(is)
}
