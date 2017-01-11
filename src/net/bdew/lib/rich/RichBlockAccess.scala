/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.rich

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.capabilities.Capability

import scala.reflect.ClassTag

class RichBlockAccess(val v: IBlockAccess) extends AnyVal {
  def getTileSafe[T: ClassTag](p: BlockPos): Option[T] = {
    val tile = v.getTileEntity(p)
    if (tile != null && implicitly[ClassTag[T]].runtimeClass.isInstance(tile))
      Some(tile.asInstanceOf[T])
    else
      None
  }

  def getBlockSafe[T: ClassTag](p: BlockPos): Option[T] = {
    val block = v.getBlockState(p).getBlock
    if (block != null && implicitly[ClassTag[T]].runtimeClass.isInstance(block))
      Some(block.asInstanceOf[T])
    else
      None
  }

  def getCapSafe[T](p: BlockPos, side: EnumFacing, cap: Capability[T]): Option[T] = {
    val te = v.getTileEntity(p)
    if (v != null)
      Option(te.getCapability(cap, side))
    else
      None
  }
}
