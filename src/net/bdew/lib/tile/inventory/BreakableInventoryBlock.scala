/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.tile.inventory

import net.minecraft.block.Block
import net.minecraft.world.World

trait BreakableInventoryBlock extends Block {
  override def breakBlock(world: World, x: Int, y: Int, z: Int, blockId: Int, meta: Int) {
    if (!world.isRemote) {
      val te = world.getBlockTileEntity(x, y, z).asInstanceOf[BreakableInventoryTile]
      if (te != null) {
        te.dropItems()
      }
    }
    super.breakBlock(world, x, y, z, blockId, meta)
  }
}
