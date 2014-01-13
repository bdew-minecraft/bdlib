/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.rotate

import net.minecraft.block.ITileEntityProvider
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection
import cpw.mods.fml.common.FMLCommonHandler

/**
 * Stores rotation data in tile entity. The TE should implement {@link RotateableTile}
 */
trait RotateableTileBlock extends BaseRotateableBlock with ITileEntityProvider {
  def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection) {
    world.getBlockTileEntity(x, y, z).asInstanceOf[RotateableTile].rotation := facing
  }

  def getFacing(world: IBlockAccess, x: Int, y: Int, z: Int): ForgeDirection = {
    val te: RotateableTile = world.getBlockTileEntity(x, y, z).asInstanceOf[RotateableTile]
    if (te == null || te.rotation == null) {
      FMLCommonHandler.instance().getFMLLogger.warning("Failed gettig rocation for block at %d, %d, %d".format(x, y, z))
      return getDefaultFacing
    }
    return te.rotation
  }
}