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
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.{IBlockAccess, World}

abstract class BlockController[T <: TileController](val name: String, material: Material, val TEClass: Class[T])
  extends Block(material) with HasTE[T] with ConnectedTextureBlock {

  def resources: ResourceProvider

  override def edgeIcon = resources.edge

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    getTE(world, x, y, z).onBreak()
    super.breakBlock(world, x, y, z, block, meta)
  }

  override def canConnect(world: IBlockAccess, origin: BlockRef, target: BlockRef) =
    getTE(world, origin).modules.contains(target)

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    getTE(world, x, y, z).onClick(player)
    return true
  }
}


