/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.bdew.lib.BdLib
import net.minecraft.block.state.IBlockState
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.{ChunkCache, IBlockAccess, World}
import net.minecraftforge.common.property.IExtendedBlockState

/**
  * Mixin for blocks that have a TileEntity
  *
  * @tparam T type of TE
  */
trait HasTE[T] extends Block with ITileEntityProvider {
  /**
    * Class of TE - must override in implementing blocks
    */
  val TEClass: Class[_ <: TileEntity]

  private def checkCast(t: TileEntity): Option[T] = {
    if (t != null && TEClass.isInstance(t))
      Some(t.asInstanceOf[T])
    else
      None
  }

  override def createNewTileEntity(world: World, meta: Int): TileEntity = TEClass.newInstance()

  override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = {
    if (state.isInstanceOf[IExtendedBlockState]) {
      getTE(world, pos).map(x => getExtendedStateFromTE(state.asInstanceOf[IExtendedBlockState], world, pos, x)).getOrElse(state)
    } else state // if block has no unlisted properties, it will use the non-extended version of BlockState and there's no point in calling this
  }

  /**
    * Get TE for a block
    */
  def getTE(w: World, pos: BlockPos): T = {
    var t = w.getTileEntity(pos)
    if ((t == null) || !TEClass.isInstance(t)) {
      if (w.getBlockState(pos).getBlock == this) {
        BdLib.logWarn("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, pos.getX, pos.getY, pos.getZ)
        t = createTileEntity(w, w.getBlockState(pos))
        w.setTileEntity(pos, t)
      } else {
        sys.error(s"Attempt to get TE ${ TEClass.getName } on a mismatched block ${ w.getBlockState(pos) } at $pos")
      }
    }
    return t.asInstanceOf[T]
  }

  /**
    * Get TE for a block, if available. If this is in client it might not be available yet.
    */
  def getTE(w: IBlockAccess, pos: BlockPos): Option[T] = {
    try {
      w match {
        case ww: ChunkCache =>
          checkCast(ww.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK))
        case ww: World =>
          Some(getTE(ww, pos))
        case _ =>
          checkCast(w.getTileEntity(pos))
      }
    } catch {
      case e: Throwable =>
        BdLib.logWarnException("Error retrieving TE of type %s at %s (world is %s)", e, getClass.getName, pos.toString, w.getClass.getName)
        None
    }
  }

  /**
    * Override to provide IExtendedBlockState from TE data
    */
  def getExtendedStateFromTE(state: IExtendedBlockState, world: IBlockAccess, pos: BlockPos, te: T): IExtendedBlockState = state
}
