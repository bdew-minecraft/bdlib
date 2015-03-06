/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.minecraft.block.Block
import net.minecraft.client.renderer.{RenderBlocks, Tessellator}
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11

object RenderUtils {
  /**
   * Render a single item side
   */
  def doRenderItemSide(d: ForgeDirection, r: RenderBlocks, block: Block, meta: Int) = {
    val icon = r.getBlockIconFromSideAndMetadata(block, d.ordinal(), meta)
    Tessellator.instance.setNormal(d.offsetX, d.offsetY, d.offsetZ)
    d match {
      case ForgeDirection.DOWN => r.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon)
      case ForgeDirection.UP => r.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon)
      case ForgeDirection.NORTH => r.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon)
      case ForgeDirection.SOUTH => r.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon)
      case ForgeDirection.WEST => r.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon)
      case ForgeDirection.EAST => r.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon)
      case _ => sys.error("Invalid side")
    }
  }

  /**
   * Render a simple block as an item (for gui, item entities etc.)
   */
  def renderSimpleBlockItem(block: Block, metadata: Int, renderer: RenderBlocks) {
    block.setBlockBoundsForItemRender()
    renderer.setRenderBoundsFromBlock(block)
    val T = Tessellator.instance
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F)
    for (side <- ForgeDirection.VALID_DIRECTIONS) {
      T.startDrawingQuads()
      doRenderItemSide(side, renderer, block, metadata)
      T.draw()
    }
    GL11.glTranslatef(0.5F, 0.5F, 0.5F)
  }
}
