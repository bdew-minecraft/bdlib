/*
 * Copyright (c) bdew, 2013 - 2015
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
import net.minecraft.util.{BlockPos, ChatComponentTranslation, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}

trait BlockRedstoneSensorModule[T <: TileRedstoneSensorModule] extends BlockModule[T] with BlockFacingSignalMeta with GuiProvider {
  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, pos)
    if (te.getCore.isDefined) {
      te.config.ensureValid(te.getCore.get)
      doOpenGui(world, pos, player)
    } else {
      player.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }

  def doOpenGui(world: World, pos: BlockPos, player: EntityPlayer)

  def notifyTarget(w: World, pos: BlockPos): Unit = {
    w.notifyBlockOfStateChange(pos.offset(getFacing(w, pos)), this)
  }

  override def setSignal(world: World, pos: BlockPos, signal: Boolean): Unit = {
    super.setSignal(world, pos, signal)
    notifyTarget(world, pos)
  }

  override def canProvidePower = true

  override def isSideSolid(world: IBlockAccess, pos: BlockPos, side: EnumFacing) = true

  override def getWeakPower(worldIn: IBlockAccess, pos: BlockPos, state: IBlockState, side: EnumFacing) =
    if (getSignal(worldIn, pos)) 15 else 0
}
