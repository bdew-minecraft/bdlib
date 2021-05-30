package net.bdew.lib.keepdata

import net.minecraft.item.{BlockItem, BlockItemUseContext, Item}
import net.minecraft.util.ActionResultType

/**
 * Custom ItemBlock that handles loading data to the Tile Entity
 */
class BlockItemKeepData(block: BlockKeepData, props: Item.Properties) extends BlockItem(block, props) {
  override def place(ctx: BlockItemUseContext): ActionResultType = {
    val original = ctx.getItemInHand.copy()
    val res = super.place(ctx)
    if (res.consumesAction() && !ctx.getLevel.isClientSide)
      block.restoreTileEntity(ctx.getLevel, ctx.getClickedPos, original, ctx.getPlayer)
    res
  }
}