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

trait BlockCoverable[T <: TileCoverable] extends HasTE[T] {
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, face: Int, xoffs: Float, yoffs: Float, zoffs: Float): Boolean = {
    val te = getTE(world, x, y, z)
    val side = Misc.forgeDirection(face)

    if (player.getCurrentEquippedItem == null && player.isSneaking) {
      for (cover <- Option(te.covers(side).cval)) {
        if (!world.isRemote) {
          te.covers(side) := null
          te.onCoversChanged()
          ItemUtils.dropItemToPlayer(world, player, cover)
        }
        return true
      }
    }

    for {
      activeStack <- Option(player.getCurrentEquippedItem)
      activeItem <- Option(activeStack.getItem)
      coverItem <- Misc.asInstanceOpt(activeItem, classOf[ItemCover])
      if te.isValidCover(side, activeStack) && coverItem.isValidTile(te, activeStack)
    } {
      if (!world.isRemote) {
        for (cover <- Option(te.covers(side).cval))
          ItemUtils.throwItemAt(world, te.xCoord + side.offsetX, te.yCoord + side.offsetY, te.zCoord + side.offsetZ, cover.copy())

        te.covers(side) := activeStack.splitStack(1)

        if (activeStack.stackSize <= 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null)
        te.onCoversChanged()
      }
      return true
    }

    super.onBlockActivated(world, x, y, z, player, face, xoffs, yoffs, zoffs)
  }

  override def getBlockTexture(w: IBlockAccess, x: Int, y: Int, z: Int, side: Int) =
    (for {
      coverStack <- Option(getTE(w, x, y, z).covers(Misc.forgeDirection(side)).cval)
      coverItem <- Option(coverStack.getItem) flatMap (Misc.asInstanceOpt(_, classOf[ItemCover]))
    } yield coverItem.getCoverIcon(coverStack)
      ) getOrElse super.getBlockTexture(w, x, y, z, side)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, blockId: Int, meta: Int) {
    if (!world.isRemote)
      for ((dir, covOpt) <- getTE(world, x, y, z).covers; cover <- Option(covOpt.cval))
        ItemUtils.throwItemAt(world, x, y, z, cover)
    super.breakBlock(world, x, y, z, blockId, meta)
  }
}
