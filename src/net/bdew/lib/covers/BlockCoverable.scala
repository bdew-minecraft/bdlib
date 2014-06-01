/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.covers

import net.bdew.lib.block.HasTE
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.Misc
import net.bdew.lib.items.ItemUtils
import net.minecraft.item.ItemStack

trait BlockCoverable[T <: TileCoverable] extends HasTE[T] {
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, face: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    val te = getTE(world, x, y, z)
    val side = Misc.forgeDirection(face)

    if (player.getCurrentEquippedItem == null && player.isSneaking) {
      for (cover <- te.covers(side)) {
        if (!world.isRemote) {
          te.covers(side) := None
          te.onCoversChanged()
          ItemUtils.dropItemToPlayer(world, player, new ItemStack(cover))
        }
        return true
      }
    }

    for {
      activeStack <- Option(player.getCurrentEquippedItem)
      activeItem <- Option(activeStack.getItem)
      coverItem <- Misc.asInstanceOpt(activeItem, classOf[ItemCover])
      if te.isValidCover(side, coverItem) && coverItem.isValidTile(te)
    } {
      if (!world.isRemote) {
        for (cover <- te.covers(side))
          ItemUtils.dropItemToPlayer(world, player, new ItemStack(cover))

        player.inventory.decrStackSize(player.inventory.currentItem, 1)
        te.covers(side) := coverItem
        te.onCoversChanged()
      }
      return true
    }

    super.onBlockActivated(world, x, y, z, player, face, xoffs, yoffs, zoffs)
  }

  override def getBlockTexture(w: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
    getTE(w, x, y, z).covers(Misc.forgeDirection(side)).cval map (_.getCoverIcon) getOrElse
      super.getBlockTexture(w, x, y, z, side)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, blockId: Int, meta: Int) {
    if (!world.isRemote)
      for ((dir, covOpt) <- getTE(world, x, y, z).covers; cover <- covOpt)
        ItemUtils.throwItemAt(world, x, y, z, new ItemStack(cover))
    super.breakBlock(world, x, y, z, blockId, meta)
  }
}
