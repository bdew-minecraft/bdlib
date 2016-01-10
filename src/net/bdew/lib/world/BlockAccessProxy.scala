/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.world

import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess

class BlockAccessProxy(base: IBlockAccess) extends IBlockAccess {
  override def isSideSolid(pos: BlockPos, side: EnumFacing, _default: Boolean) = base.isSideSolid(pos, side, _default)
  override def getWorldType = base.getWorldType
  override def getCombinedLight(pos: BlockPos, lightValue: Int) = base.getCombinedLight(pos, lightValue)
  override def getTileEntity(pos: BlockPos) = base.getTileEntity(pos)
  override def extendedLevelsInChunkCache() = base.extendedLevelsInChunkCache()
  override def isAirBlock(pos: BlockPos) = base.isAirBlock(pos)
  override def getBiomeGenForCoords(pos: BlockPos) = base.getBiomeGenForCoords(pos)
  override def getBlockState(pos: BlockPos) = base.getBlockState(pos)
  override def getStrongPower(pos: BlockPos, direction: EnumFacing) = base.getStrongPower(pos, direction)
}
