package net.bdew.lib.keepdata

import net.bdew.lib.block.HasTE
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.{BlockGetter, Level}

/**
 * Mixin for blocks to keep their data when broken
 * Must have a TileEntity that extends TileKeepData
 */
trait BlockKeepData extends Block {
  this: HasTE[_ <: TileKeepData] =>

  override def getCloneItemStack(world: BlockGetter, pos: BlockPos, state: BlockState): ItemStack = {
    val stack = super.getCloneItemStack(world, pos, state)
    getTE(world, pos).foreach(_.saveDataToItem(stack))
    stack
  }

  /**
   * Override for special actions on placing
   * ItemStack might or might not contain actual data
   * Called with the TE created and added to world, after onBlockPlacedBy & co
   */
  def restoreTileEntity(world: Level, pos: BlockPos, is: ItemStack, player: Player) =
    getTE(world, pos).loadDataFromItem(is)
}
