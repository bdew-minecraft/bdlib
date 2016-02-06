/*
 * Copyright (c) bdew, 2013 - 2016
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
import net.minecraft.util.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.property.IExtendedBlockState
import net.minecraftforge.fml.common.FMLCommonHandler

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

  override def createNewTileEntity(world: World, meta: Int): TileEntity = TEClass.newInstance()

  override def getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = {
    val stateEx = state.asInstanceOf[IExtendedBlockState]
    getTE(world, pos).map(x => getExtendedStateFromTE(stateEx, world, pos, x)).getOrElse(stateEx)
  }

  /**
    * Get TE for a block
    */
  def getTE(w: World, pos: BlockPos): T = {
    var t = w.getTileEntity(pos)
    if ((t == null) || !TEClass.isInstance(t)) {
      BdLib.logWarn("Tile entity for block %s at (%d,%d,%d) is corrupt or missing - recreating", this, pos.getX, pos.getY, pos.getZ)
      t = createNewTileEntity(w, getMetaFromState(w.getBlockState(pos)))
      w.setTileEntity(pos, t)
    }
    return t.asInstanceOf[T]
  }

  /**
    * Get TE for a block, if available. If this is in client it might not be available yet.
    */
  def getTE(w: IBlockAccess, pos: BlockPos): Option[T] = {
    val t = w.getTileEntity(pos)
    if ((t == null) || !TEClass.isInstance(t)) {
      w match {
        case ww: World if FMLCommonHandler.instance().getEffectiveSide.isServer => Some(getTE(ww, pos))
        case _ => None
      }
    } else Some(t.asInstanceOf[T])
  }

  /**
    * Override to provide IExtendedBlockState from TE data
    */
  def getExtendedStateFromTE(state: IExtendedBlockState, world: IBlockAccess, pos: BlockPos, te: T): IExtendedBlockState = state
}
