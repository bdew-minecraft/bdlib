/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BaseBlock
import net.minecraft.block.state.IBlockState
import net.minecraft.util._
import net.minecraft.util.math.{BlockPos, Vec3i}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.IExtendedBlockState

trait ConnectedTextureBlock extends BaseBlock {
  override def getUnlistedProperties = super.getUnlistedProperties :+ ConnectionsProperty

  override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos) =
    super.getExtendedState(state, world, pos).asInstanceOf[IExtendedBlockState]
      .withProperty(ConnectionsProperty, getConnections(world, pos).toMap)

  def getConnections(world: IBlockAccess, pos: BlockPos) =
    for (x <- -1 to +1; y <- -1 to +1; z <- -1 to +1 if x != 0 || y != 0 || z != 0) yield {
      val vector = new Vec3i(x, y, z)
      vector -> canConnect(world, pos, pos.add(vector))
    }

  override def canRenderInLayer(state: IBlockState, layer: BlockRenderLayer): Boolean =
    layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT

  def canConnect(world: IBlockAccess, origin: BlockPos, target: BlockPos): Boolean
}

