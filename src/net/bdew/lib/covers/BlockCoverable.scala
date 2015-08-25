/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc
import net.bdew.lib.block.{BlockRef, HasTE}
import net.bdew.lib.items.ItemUtils
import net.minecraft.block.Block
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

trait BlockCoverable[T <: TileCoverable] extends HasTE[T] {
  @SideOnly(Side.CLIENT)
  override def getRenderType = CoverRenderer.id

  private def getCoverItem(w: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection): Option[(ItemCover, ItemStack, TileCoverable)] = {
    val te = getTE(w, x, y, z)
    for {
      coverStack <- Option(te.covers(side).value)
      coverItemMaybe <- Option(coverStack.getItem)
      coverItem <- Misc.asInstanceOpt(coverItemMaybe, classOf[ItemCover])
    } yield (coverItem, coverStack, te)
  }

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, face: Int, xOffs: Float, yOffs: Float, zOffs: Float): Boolean = {
    val te = getTE(world, x, y, z)
    val side = Misc.forgeDirection(face)

    // Shift+RightClick with empty hand should remove cover
    if (player.getCurrentEquippedItem == null && player.isSneaking) {
      for {
        cover <- Option(te.covers(side).value)
        coverItem <- Misc.asInstanceOpt(cover.getItem, classOf[ItemCover])
      } {
        if (!world.isRemote) {
          te.covers(side) := null
          te.onCoversChanged()
          coverChanged(world, x, y, z, side)
          ItemUtils.dropItemToPlayer(world, player, coverItem.onRemove(cover))
        }
        return true
      }
    }

    // If held item is cover - try to install it
    for {
      activeStack <- Option(player.getCurrentEquippedItem)
      activeItem <- Option(activeStack.getItem)
      coverItem <- Misc.asInstanceOpt(activeItem, classOf[ItemCover])
      if te.isValidCover(side, activeStack) && coverItem.isValidTile(te, activeStack)
    } {
      if (!world.isRemote && player.isInstanceOf[EntityPlayerMP]) {
        for {
          oldCover <- Option(te.covers(side).value)
          oldItem <- Misc.asInstanceOpt(oldCover.getItem, classOf[ItemCover])
        } {
          ItemUtils.throwItemAt(world, te.xCoord + side.offsetX, te.yCoord + side.offsetY, te.zCoord + side.offsetZ, oldItem.onRemove(oldCover))
        }

        te.covers(side) := coverItem.onInstall(te, side, activeStack.splitStack(1), player.asInstanceOf[EntityPlayerMP])

        if (activeStack.stackSize <= 0)
          player.inventory.setInventorySlotContents(player.inventory.currentItem, null)

        te.onCoversChanged()
        coverChanged(world, x, y, z, side)
      }
      return true
    }

    // If has cover - let it handle the click
    for ((item, stack, te) <- getCoverItem(world, x, y, z, side)) {
      if (item.clickCover(te, side, stack, player)) return true
    }

    super.onBlockActivated(world, x, y, z, player, face, xOffs, yOffs, zOffs)
  }

  override def canProvidePower = true

  override def isProvidingWeakPower(w: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Int = {
    val direction = Misc.forgeDirection(side).getOpposite
    for ((item, stack, te) <- getCoverItem(w, x, y, z, direction)) {
      if (item.isEmittingSignal(te, direction, stack)) return 15
    }
    0
  }

  def coverChanged(w: World, x: Int, y: Int, z: Int, side: ForgeDirection): Unit = {
    val pos = BlockRef(x, y, z).neighbour(side)
    w.notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, pos.block(w).orNull)
  }

  def getCoverIcon(w: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection): Option[IIcon] =
    for ((item, stack, te) <- getCoverItem(w, x, y, z, side))
      yield item.getCoverIcon(te, side, stack)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    if (!world.isRemote) {
      val te = getTE(world, x, y, z)
      for {
        (dir, covOpt) <- te.covers
        cover <- Option(covOpt.value)
        item <- Misc.asInstanceOpt(cover.getItem, classOf[ItemCover])
      } {
        te.covers(dir) := null
        ItemUtils.throwItemAt(world, x, y, z, item.onRemove(cover))
        coverChanged(world, x, y, z, dir)
      }
    }
    super.breakBlock(world, x, y, z, block, meta)
  }
}
