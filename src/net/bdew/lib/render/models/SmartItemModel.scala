/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import java.util
import javax.vecmath.Matrix4f

import com.google.common.collect.ImmutableList
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.{BakedQuad, IBakedModel, ItemOverrideList}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import org.apache.commons.lang3.tuple.Pair

/**
  * Provides a saner replacement to ISmartItemModel (r.i.p) via ItemOverrides
  */
trait SmartItemModel extends IBakedModel {
  /**
    * Override this to provide quads for items. Normal getQuads is not called for them.
    */
  def getItemQuads(stack: ItemStack, side: EnumFacing, transformType: TransformType, rand: Long): util.List[BakedQuad]

  final override def getOverrides: ItemOverrideList = ItemOverrides

  private object ItemOverrides extends ItemOverrideList(ImmutableList.of()) {
    override def handleItemState(originalModel: IBakedModel, stack: ItemStack, world: World, entity: EntityLivingBase): IBakedModel = {
      new ItemModel(stack, null)
    }
  }

  private class ItemModel(stack: ItemStack, mode: TransformType) extends BakedModelProxy(this) {
    override def getQuads(state: IBlockState, side: EnumFacing, rand: Long): util.List[BakedQuad] = {
      getItemQuads(stack, side, mode, rand)
    }
    override def handlePerspective(cameraTransformType: TransformType): Pair[_ <: IBakedModel, Matrix4f] = {
      val ret = super.handlePerspective(cameraTransformType)
      if (ret != null && ret.getLeft == this)
        Pair.of(new ItemModel(stack, cameraTransformType), ret.getRight)
      else
        ret
    }
  }

}