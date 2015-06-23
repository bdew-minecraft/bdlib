/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.bdew.lib.block.HasTE
import net.minecraft.block.Block
import net.minecraft.world.World

trait BreakableInventoryBlock extends Block {
  this: HasTE[_ <: BreakableInventoryTile] =>
  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    if (!world.isRemote) {
      getTE(world, x, y, z).dropItems()
    }
    super.breakBlock(world, x, y, z, block, meta)
  }
}
