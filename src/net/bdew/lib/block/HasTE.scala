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
import net.minecraft.world.{IBlockAccess, World}

trait HasTE[T] extends Block with ITileEntityProvider {
  val TEClass: Class[_ <: TileEntity]

  def createNewTileEntity(world: World, meta: Int): TileEntity = TEClass.newInstance()

  def getTE(w: IBlockAccess, ref: BlockRef): T = getTE(w, ref.x, ref.y, ref.z)

  def getTE(w: IBlockAccess, x: Int, y: Int, z: Int): T = {
    var t = w.getTileEntity(x, y, z)
    if ((t == null) || !TEClass.isInstance(t)) {
      BdLib.logError("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, x, y, z)
      w match {
        case ww: World =>
          t = createNewTileEntity(ww, w.getBlockMetadata(x, y, z))
          ww.setTileEntity(x, y, z, t)
        case _ =>
          sys.error("Unable to recreate TE")
      }
    }
    return t.asInstanceOf[T]
  }
}
