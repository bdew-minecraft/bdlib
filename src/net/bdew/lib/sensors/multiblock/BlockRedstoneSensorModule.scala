/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.rotate.BlockFacingSignalMeta
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

trait BlockRedstoneSensorModule[T <: TileRedstoneSensorModule] extends BlockModule[T] with BlockFacingSignalMeta with GuiProvider {

  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, pos)
    if (te.getCore.isDefined) {
      te.config.ensureValid(te.getCore.get)
      doOpenGui(world, pos, player)
    } else {
      player.sendStatusMessage(new TextComponentTranslation("bdlib.multiblock.notconnected"), true)
    }
    true
  }

  def doOpenGui(world: World, pos: BlockPos, player: EntityPlayer)

  def notifyTarget(w: World, pos: BlockPos): Unit = {
    w.neighborChanged(pos.offset(getFacing(w, pos)), this, pos)
  }

  override def setSignal(world: World, pos: BlockPos, signal: Boolean): Unit = {
    super.setSignal(world, pos, signal)
    notifyTarget(world, pos)
  }

  override def canProvidePower(state: IBlockState) = true

  override def isSideSolid(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing) = true

  override def getWeakPower(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Int =
    if (side == getFacing(state).getOpposite && getSignal(state)) 15 else 0
}
