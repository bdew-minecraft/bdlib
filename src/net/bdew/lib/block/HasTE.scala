/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import net.bdew.lib.BdLib
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.{IBlockAccess, World}

trait HasTE[T] extends Block with ITileEntityProvider {
  val TEClass: Class[_ <: TileEntity]

  def createNewTileEntity(world: World, meta: Int): TileEntity = TEClass.newInstance()

  def getTE(w: IBlockAccess, pos: BlockPos): T = {
    var t = w.getTileEntity(pos)
    if ((t == null) || !TEClass.isInstance(t)) {
      BdLib.logError("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, pos.getX, pos.getY, pos.getZ)
      w match {
        case ww: World =>
          t = createNewTileEntity(ww, getMetaFromState(w.getBlockState(pos)))
          ww.setTileEntity(pos, t)
        case _ =>
          sys.error("Unable to recreate TE")
      }
    }
    return t.asInstanceOf[T]
  }
}
