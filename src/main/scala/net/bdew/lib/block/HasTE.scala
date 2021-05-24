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
import net.bdew.lib.managers.TEManager
import net.minecraft.block.{Block, BlockState}
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}

/**
 * Mixin for blocks that have a TileEntity
 *
 * @tparam T type of TE
 */
trait HasTE[T <: TileEntity] extends Block {
  lazy val teType: TileEntityType[T] = TEManager.blockMap(this).asInstanceOf[TileEntityType[T]]

  override def hasTileEntity(state: BlockState): Boolean = true

  override def createTileEntity(state: BlockState, world: IBlockReader): T = teType.create()

  /**
   * Get TE for a block
   */
  def getTE(w: World, pos: BlockPos): T = {
    var t = teType.getBlockEntity(w, pos)
    if (t == null) {
      if (w.getBlockState(pos).getBlock == this) {
        BdLib.logWarn("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, pos.getX, pos.getY, pos.getZ)
        t = createTileEntity(w.getBlockState(pos), w)
        w.setBlockEntity(pos, t)
      } else {
        sys.error(s"Attempt to get TE ${teType.getRegistryName} on a mismatched block ${w.getBlockState(pos)} at $pos")
      }
    }
    t
  }

  /**
   * Get TE for a block, if available. If this is in client it might not be available yet.
   */
  def getTE(w: IBlockReader, pos: BlockPos): Option[T] = {
    try {
      w match {
        case ww: World =>
          Some(getTE(ww, pos))
        case _ =>
          Option(teType.getBlockEntity(w, pos))
      }
    } catch {
      case e: Throwable =>
        BdLib.logWarnException("Error retrieving TE of type %s at %s (world is %s)", e, getClass.getName, pos.toString, w.getClass.getName)
        None
    }
  }
}
