/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.model.{BakedQuad, IBakedModel, ItemOverrideList}
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraftforge.client.ForgeHooksClient

import java.util
import java.util.Random

/**
 * Provides a saner replacement to ISmartItemModel (RIP) via ItemOverrides
 */
trait SmartItemModel extends IBakedModel {
  /**
   * Override this to provide quads for items. Normal getQuads is not called for them.
   */
  def getItemQuads(stack: ItemStack, side: Direction, transformType: TransformType, rand: Random): util.List[BakedQuad]

  final override def getOverrides: ItemOverrideList = ItemOverrides

  private object ItemOverrides extends ItemOverrideList() {
    override def resolve(originalModel: IBakedModel, stack: ItemStack, world: ClientWorld, entity: LivingEntity): IBakedModel = {
      new ItemModel(stack, null)
    }
  }

  private class ItemModel(stack: ItemStack, transform: TransformType) extends BakedModelProxy(this) {
    override def getQuads(state: BlockState, side: Direction, rand: Random): util.List[BakedQuad] = {
      getItemQuads(stack, side, transform, rand)
    }

    override def handlePerspective(cameraTransformType: TransformType, mat: MatrixStack): IBakedModel =
      ForgeHooksClient.handlePerspective(new ItemModel(stack, cameraTransformType), cameraTransformType, mat)
  }
}