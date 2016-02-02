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
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.IExtendedBlockState

trait BlockOutput[T <: TileOutput[_]] extends BlockModule[T] {
  override def getUnlistedProperties = super.getUnlistedProperties :+ OutputFaceProperty

  override def getExtendedStateFromTE(state: IExtendedBlockState, world: IBlockAccess, pos: BlockPos, te: T) = {
    val faces = for {
      core <- te.getCore.toIterable
      face <- EnumFacing.values()
      output <- core.outputFaces.get(BlockFace(pos, face))
    } yield face -> output
    super.getExtendedStateFromTE(state, world, pos, te).withProperty(OutputFaceProperty, faces.toMap)
  }

  override def colorMultiplier(worldIn: IBlockAccess, pos: BlockPos, index: Int) =
    resources.outputColors.getOrElse(index, Color.white).asRGB
}
