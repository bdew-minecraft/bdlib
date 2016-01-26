/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.connected

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.{BaseBlock, BlockFace}
import net.bdew.lib.property.SimpleUnlistedProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.util._
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.IExtendedBlockState

trait ConnectedTextureBlock extends BaseBlock {
  override def getUnlistedProperties =
    super.getUnlistedProperties ++ ConnectedTextureBlock.OVERLAYS.values :+ ConnectedTextureBlock.CONNECTIONS

  override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos) = {
    val connections = for (x <- -1 to +1; y <- -1 to +1; z <- -1 to +1 if x != 0 || y != 0 || z != 0) yield {
      val vector = new Vec3i(x, y, z)
      vector -> canConnect(world, pos, pos.add(vector))
    }
    super.getExtendedState(state, world, pos).asInstanceOf[IExtendedBlockState]
      .withProperty(ConnectedTextureBlock.CONNECTIONS, connections.toMap)
      .withPropertiesEx(EnumFacing.values().map(f => ConnectedTextureBlock.OVERLAYS(f) -> getOverlays(world, BlockFace(pos, f))))
  }

  override def canRenderInLayer(layer: EnumWorldBlockLayer) =
    layer == EnumWorldBlockLayer.SOLID || layer == EnumWorldBlockLayer.CUTOUT

  def canConnect(world: IBlockAccess, origin: BlockPos, target: BlockPos): Boolean

  def getOverlays(world: IBlockAccess, face: BlockFace): List[ConnectedTextureBlock.Overlay]
}

object ConnectedTextureBlock {

  case class Overlay(texture: ResourceLocation, rotation: Int, tint: Int = -1, shading: Boolean = false)

  val CONNECTIONS = new SimpleUnlistedProperty[Map[Vec3i, Boolean]]("connections", classOf[Map[Vec3i, Boolean]])
  val OVERLAYS = EnumFacing.values().map(f => f -> new SimpleUnlistedProperty[List[Overlay]](f.getName, classOf[List[Overlay]])).toMap
}