/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

import scala.reflect.ClassTag

case class BlockRef(x: Int, y: Int, z: Int) {
  def isValid(w: IBlockAccess) = y >= 0 && y < 256 // hardcoded because getHeight is unavailable on servers :(

  def block(w: IBlockAccess) = if (isValid(w)) Option(w.getBlock(x, y, z)) else None
  def tile(w: IBlockAccess) = if (isValid(w)) Option(w.getTileEntity(x, y, z)) else None
  def meta(w: IBlockAccess) = if (isValid(w)) w.getBlockMetadata(x, y, z) else 0

  def blockIs(w: IBlockAccess, other: Block) = block(w).contains(other)

  def neighbour(side: ForgeDirection) = BlockRef(x + side.offsetX, y + side.offsetY, z + side.offsetZ)
  def neighbours = ForgeDirection.VALID_DIRECTIONS.map(x => x -> neighbour(x)).toMap

  override def toString = "(x=%d y=%d z=%d)".format(x, y, z)

  def getTile[T: ClassTag](w: IBlockAccess) = tile(w) flatMap { tile =>
    val cls = implicitly[ClassTag[T]].runtimeClass
    if (cls.isInstance(tile))
      Some(tile.asInstanceOf[T])
    else None
  }

  def ==(tx: Int, ty: Int, tz: Int) = x == tx && y == ty && z == tz

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