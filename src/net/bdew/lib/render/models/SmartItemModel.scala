package net.bdew.lib.render.models

import java.util

import com.google.common.collect.ImmutableList
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.{BakedQuad, IBakedModel, ItemOverrideList}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.world.World

/**
  * Provides a saner replacement to ISmartItemModel (r.i.p) via ItemOverrides
  */
trait SmartItemModel extends IBakedModel {
  /**
    * Override this to provide quads for items. Normal getQuads is not called for them.
    */
  def getItemQuads(stack: ItemStack, side: EnumFacing, rand: Long): util.List[BakedQuad]

  final override def getOverrides: ItemOverrideList = ItemOverrides

  private object ItemOverrides extends ItemOverrideList(ImmutableList.of()) {
    override def handleItemState(originalModel: IBakedModel, stack: ItemStack, world: World, entity: EntityLivingBase): IBakedModel = {
      new ItemModel(stack)
    }
  }

  private class ItemModel(stack: ItemStack) extends BakedModelProxy(this) {
    override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] = {
      getItemQuads(stack, side, rand)
    }
  }

}