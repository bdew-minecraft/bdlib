/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.sensors.multiblock

import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.rotate.{BaseRotatableBlock, IconType}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{ChatComponentTranslation, IIcon}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

trait BlockRedstoneSensorModule[T <: TileRedstoneSensorModule] extends BlockModule[T] with BaseRotatableBlock with GuiProvider {
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xOffs: Float, yOffs: Float, zOffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, x, y, z)
    if (te.getCore.isDefined) {
      te.config.ensureValid(te.getCore.get)
      doOpenGui(world, x, y, z, player)
    } else {
      player.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }

  def doOpenGui(world: World, x: Int, y: Int, z: Int, player: EntityPlayer)

  def notifyTarget(w: World, x: Int, y: Int, z: Int): Unit = {
    val bp = BlockRef(x, y, z).neighbour(getFacing(w, x, y, z))
    w.notifyBlocksOfNeighborChange(bp.x, bp.y, bp.z, this)
  }

  override def getFacing(world: IBlockAccess, x: Int, y: Int, z: Int) =
    Misc.forgeDirection(world.getBlockMetadata(x, y, z) & 7)

  override def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection) = {
    val m = world.getBlockMetadata(x, y, z) & 8
    world.setBlockMetadataWithNotify(x, y, z, m | facing.ordinal(), 3)
    notifyTarget(world, x, y, z)
  }

  def isSignalOn(world: IBlockAccess, x: Int, y: Int, z: Int) =
    (world.getBlockMetadata(x, y, z) & 8) == 8

  def setSignal(world: World, x: Int, y: Int, z: Int, signal: Boolean): Unit = {
    val m = world.getBlockMetadata(x, y, z) & 7
    world.setBlockMetadataWithNotify(x, y, z, m | (if (signal) 8 else 0), 3)
    notifyTarget(world, x, y, z)
  }

  override def getIcon(meta: Int, kind: IconType.Value) = kind match {
    case IconType.BACK => bottomIcon
    case IconType.FRONT => if ((meta & 8) == 8) frontIconOn else frontIcon
    case IconType.SIDE => if ((meta & 8) == 8) sideIconOn else sideIcon
  }

  override def canProvidePower = true

  override def isSideSolid(world: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection) = true

  override def isProvidingWeakPower(w: IBlockAccess, x: Int, y: Int, z: Int, side: Int) = {
    val m = w.getBlockMetadata(x, y, z)
    if (((m & 7) == Misc.forgeDirection(side).getOpposite.ordinal()) && ((m & 8) == 8))
      15
    else
      0
  }

  var frontIcon: IIcon = null
  var bottomIcon: IIcon = null
  var frontIconOn: IIcon = null
  var sideIconOn: IIcon = null
  var sideIcon: IIcon = null
}
