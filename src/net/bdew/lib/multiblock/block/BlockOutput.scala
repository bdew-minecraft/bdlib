/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.block.BlockFace
import net.bdew.lib.gui.Color
import net.bdew.lib.multiblock.render.OutputFaceProperty
import net.bdew.lib.multiblock.tile.TileOutput
import net.minecraft.block.state.IBlockState
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess

trait BlockOutput[T <: TileOutput[_]] extends BlockModule[T] {
  override def getUnlistedProperties = super.getUnlistedProperties :+ OutputFaceProperty
  override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos) = {
    val faces = for {
      core <- getTE(world, pos).getCore.toList
      face <- EnumFacing.values()
      output <- core.outputFaces.get(BlockFace(pos, face))
    } yield face -> output
    super.getExtendedState(state, world, pos).withProperty(OutputFaceProperty, faces.toMap)
  }

  override def colorMultiplier(worldIn: IBlockAccess, pos: BlockPos, index: Int) =
    resources.outputColors.getOrElse(index, Color.white).asRGB
}
