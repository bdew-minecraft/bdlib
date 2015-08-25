/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.bdew.lib.Misc
import net.bdew.lib.render.connected.ConnectedHelper
import net.bdew.lib.render.{BaseBlockRenderHandler, RenderUtils}
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

object CoverRenderer extends BaseBlockRenderHandler {
  override def renderInventoryBlock(block: Block, metadata: Int, modelId: Int, renderer: RenderBlocks) =
    RenderUtils.renderSimpleBlockItem(block, metadata, renderer)

  override def renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks) = {
    renderer.renderStandardBlock(block, x, y, z)
    if (!renderer.hasOverrideBlockTexture)
      for {
        blockCov <- Misc.asInstanceOpt(block, classOf[BlockCoverable[_]])
        face <- ForgeDirection.VALID_DIRECTIONS
        icon <- blockCov.getCoverIcon(world, x, y, z, face)
        if block.shouldSideBeRendered(world, x + face.offsetX, y + face.offsetY, z + face.offsetZ, face.ordinal())
      } ConnectedHelper.draw(face, 8).doDraw(ConnectedHelper.Vec3F(x, y, z), icon)
    true
  }
}
