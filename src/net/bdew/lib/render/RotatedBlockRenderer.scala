/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.render.connected.ConnectedHelper.{EdgeDraw, RectF}
import net.bdew.lib.rotate.BaseRotatableBlock
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

object RotatedBlockRenderer extends BaseBlockRenderHandler {
  val filterIconDraw =
    (for (dir <- ForgeDirection.VALID_DIRECTIONS)
    yield (dir, new EdgeDraw(RectF(0.35F, 0.35F, 0.65F, 0.65F), dir))).toMap

  override def renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
    RenderUtils.renderSimpleBlockItem(block, metadata, renderer)
  }

  override def renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean = {
    block.asInstanceOf[BaseRotatableBlock].getFacing(world, x, y, z).ordinal() match {
      case 0 =>
        renderer.uvRotateEast = 3
        renderer.uvRotateWest = 3
        renderer.uvRotateSouth = 3
        renderer.uvRotateNorth = 3
      case 2 =>
        renderer.uvRotateSouth = 1
        renderer.uvRotateNorth = 2
      case 3 =>
        renderer.uvRotateSouth = 2
        renderer.uvRotateNorth = 1
        renderer.uvRotateTop = 3
        renderer.uvRotateBottom = 3
      case 4 =>
        renderer.uvRotateEast = 1
        renderer.uvRotateWest = 2
        renderer.uvRotateTop = 2
        renderer.uvRotateBottom = 1
      case 5 =>
        renderer.uvRotateEast = 2
        renderer.uvRotateWest = 1
        renderer.uvRotateTop = 1
        renderer.uvRotateBottom = 2
      case _ =>
    }

    renderer.renderStandardBlock(block, x, y, z)
    renderer.uvRotateEast = 0
    renderer.uvRotateWest = 0
    renderer.uvRotateSouth = 0
    renderer.uvRotateNorth = 0
    renderer.uvRotateTop = 0
    renderer.uvRotateBottom = 0

    true
  }
}