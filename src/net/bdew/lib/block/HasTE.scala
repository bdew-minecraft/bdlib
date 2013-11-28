/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.block

import net.minecraft.block.{ITileEntityProvider, Block}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.tileentity.TileEntity
import cpw.mods.fml.common.FMLCommonHandler

trait HasTE[T] extends Block with ITileEntityProvider {
  val TEClass: Class[_ <: TileEntity]
  def createNewTileEntity(world: World): TileEntity = TEClass.newInstance()
  def getTE(w: IBlockAccess, x: Int, y: Int, z: Int): T = {
    var t = w.getBlockTileEntity(x, y, z)
    if ((t == null) || !TEClass.isInstance(t)) {
      FMLCommonHandler.instance().getFMLLogger.severe("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating".format(this, x, y, z))
      w match {
        case ww: World =>
          t = createNewTileEntity(ww)
          ww.setBlockTileEntity(x, y, z, t)
        case _ =>
          sys.error("Unable to recreate TE")
      }
    }
    return t.asInstanceOf[T]
  }
}
