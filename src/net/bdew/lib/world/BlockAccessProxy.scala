/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.world

import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

class BlockAccessProxy(base: IBlockAccess) extends IBlockAccess {
  override def getBlock(x: Int, y: Int, z: Int) =
    base.getBlock(x, y, z)

  override def getBlockMetadata(x: Int, y: Int, z: Int) =
    base.getBlockMetadata(x, y, z)

  override def getTileEntity(x: Int, y: Int, z: Int) =
    base.getTileEntity(x, y, z)

  override def isSideSolid(x: Int, y: Int, z: Int, side: ForgeDirection, default: Boolean) =
    base.isSideSolid(x, y, z, side, default)

  override def getHeight = base.getHeight

  override def getLightBrightnessForSkyBlocks(x: Int, y: Int, z: Int, lightValue: Int) =
    base.getLightBrightnessForSkyBlocks(x, y, z, lightValue)

  override def isBlockProvidingPowerTo(x: Int, y: Int, z: Int, side: Int) =
    base.isBlockProvidingPowerTo(x, y, z, side)

  override def extendedLevelsInChunkCache() = base.extendedLevelsInChunkCache()

  override def isAirBlock(x: Int, y: Int, z: Int) =
    base.isAirBlock(x, y, z)

  override def getBiomeGenForCoords(x: Int, z: Int) =
    base.getBiomeGenForCoords(x, z)
}
