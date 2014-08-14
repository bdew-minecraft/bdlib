/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.block.{BlockRef, HasTE}
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.multiblock.{ResourceProvider, Tools}
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.world.{IBlockAccess, World}

abstract class BlockModule[T <: TileModule](val name: String, val kind: String, material: Material, val TEClass: Class[T])
  extends Block(material) with HasTE[T] with ConnectedTextureBlock {

  def resources: ResourceProvider

  override def edgeIcon = resources.edge

  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean =
    Tools.findConnections(world, BlockRef(x, y, z), kind).size <= 1

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, player: EntityLivingBase, stack: ItemStack) {
    getTE(world, x, y, z).tryConnect()
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    getTE(world, x, y, z).tryConnect()
  }

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    getTE(world, x, y, z).onBreak()
    super.breakBlock(world, x, y, z, block, meta)
  }

  override def canConnect(world: IBlockAccess, origin: BlockRef, target: BlockRef) = {
    val me = getTE(world, origin)
    me.connected.contains(target) ||
      (target.getTile[TileModule](world) exists { other =>
        me.getCore.isDefined && other.getCore == me.getCore
      })
  }

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, x, y, z)
    (for {
      p <- te.connected.cval
      bl <- p.block(world)
    } yield {
      bl.onBlockActivated(world, p.x, p.y, p.z, player, meta, 0, 0, 0)
    }) getOrElse {
      player.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }
}
