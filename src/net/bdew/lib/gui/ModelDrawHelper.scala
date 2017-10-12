/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.bdew.lib.Client
import net.bdew.lib.render.models.ModelUtils
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{BlockRenderLayer, EnumFacing}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11

object ModelDrawHelper {

  /**
    * Draws a face from a block, as seen in the world into GUI
    *
    * @param world world object
    * @param pos   position of block
    * @param face  face of block
    * @param rect  area in GUI to draw into
    */
  def renderWorldBlockIntoGUI(world: IBlockAccess, pos: BlockPos, face: EnumFacing, rect: Rect): Unit = {
    // Warning, hackery incoming.
    // This is bastardized from both item and block drawing code in vanilla as neither does exactly what we need.

    val dispatcher = Client.minecraft.getBlockRendererDispatcher
    val textures = Client.textureManager
    val blockState = world.getBlockState(pos)
    val tessellator = Tessellator.getInstance()
    val buffer = tessellator.getBuffer

    // Make sure that block atlas is selected

    textures.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    textures.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false)

    GL11.glPushMatrix()

    // Move to center of render area
    GL11.glTranslatef(rect.x1 + rect.w / 2, rect.y1 + rect.h / 2, 100.0F)

    // Scale to size of render area
    GL11.glScalef(rect.w, rect.h, 1)

    // GUI Y coordinates are top->down, while for blocks it's bottom->up, flip everything
    GL11.glRotatef(180, 1, 0, 0)

    // Rotate to the face we are rendering
    face match {
      case EnumFacing.NORTH =>
        GL11.glRotatef(180, 0, 1, 0)
      case EnumFacing.SOUTH =>
      // nothing
      case EnumFacing.WEST =>
        GL11.glRotatef(90, 0, 1, 0)
      case EnumFacing.EAST =>
        GL11.glRotatef(270, 0, 1, 0)
      case EnumFacing.UP =>
        GL11.glRotatef(90, 1, 0, 0)
      case EnumFacing.DOWN =>
        GL11.glRotatef(270, 1, 0, 0)
      case _ =>
    }

    // And go back to corner
    GL11.glTranslatef(-0.5f, -0.5f, -0.5f)

    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM)

    val model = dispatcher.getModelForState(blockState)
    val fullState = blockState.getBlock.getExtendedState(blockState, world, pos)

    // If blocks renders in multiple layers - go through all
    for (layer <- BlockRenderLayer.values() if blockState.getBlock.canRenderInLayer(blockState, layer)) {

      // This is thread local so will hopefully not break anything else. No other way to pass the current layer to the model
      ForgeHooksClient.setRenderLayer(layer)

      // Grab all relevant quads
      for (quad <- ModelUtils.getAllQuads(model, fullState)) {
        if (quad.hasTintIndex) // Apply color from block
          net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quad,
            0xFF000000 | Client.blockColors.colorMultiplier(fullState, world, pos, quad.getTintIndex))
        else // default to white
          net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quad, -1)
      }
    }

    ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID)

    // And we're done!

    tessellator.draw()

    textures.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()

    GL11.glPopMatrix()
  }

}
