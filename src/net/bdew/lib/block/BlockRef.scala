/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.block

import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

import scala.reflect.ClassTag

case class BlockRef(x: Int, y: Int, z: Int) {
  def block(w: IBlockAccess) = Option(w.getBlock(x, y, z))
  def tile(w: IBlockAccess) = Option(w.getTileEntity(x, y, z))
  def meta(w: IBlockAccess) = w.getBlockMetadata(x, y, z)

  def blockIs(w: IBlockAccess, other: Block) = block(w).contains(other)

  def neighbour(side: ForgeDirection) = BlockRef(x + side.offsetX, y + side.offsetY, z + side.offsetZ)
  def neighbours = ForgeDirection.VALID_DIRECTIONS.map(x => x -> neighbour(x))

  override def toString = "(x=%d y=%d z=%d)".format(x, y, z)

  def getTile[T: ClassTag](w: IBlockAccess) = tile(w) flatMap { tile =>
    val cls = implicitly[ClassTag[T]].runtimeClass
    if (cls.isInstance(tile))
      Some(tile.asInstanceOf[T])
    else None
  }

  def getBlock[T: ClassTag](w: IBlockAccess) = block(w) flatMap { block =>
    val cls = implicitly[ClassTag[T]].runtimeClass
    if (cls.isInstance(block))
      Some(block.asInstanceOf[T])
    else None
  }

  def writeToNBT(tag: NBTTagCompound) {
    tag.setInteger("x", x)
    tag.setInteger("y", y)
    tag.setInteger("z", z)
  }
}

object BlockRef {
  def fromTile(te: TileEntity) = BlockRef(te.xCoord, te.yCoord, te.zCoord)
  def fromNBT(tag: NBTTagCompound) =
    BlockRef(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"))
}