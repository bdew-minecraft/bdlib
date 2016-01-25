/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.covers

import net.bdew.lib.Misc
import net.bdew.lib.block.HasTE
import net.bdew.lib.items.ItemUtils
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}

trait BlockCoverable extends Block {
  self: HasTE[_ <: TileCoverable] =>

  private def getCoverItem(w: IBlockAccess, pos: BlockPos, side: EnumFacing): Option[(ItemCover, ItemStack, TileCoverable)] = {
    val te = getTE(w, pos)
    for {
      coverStack <- te.covers(side)
      coverItemMaybe <- Option(coverStack.getItem)
      coverItem <- Misc.asInstanceOpt(coverItemMaybe, classOf[ItemCover])
    } yield (coverItem, coverStack, te)
  }

  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val te = getTE(world, pos)

    // Shift+RightClick with empty hand should remove cover
    if (player.getCurrentEquippedItem == null && player.isSneaking) {
      for {
        cover <- te.covers(side)
        coverItem <- Misc.asInstanceOpt(cover.getItem, classOf[ItemCover])
      } {
        if (!world.isRemote) {
          te.covers(side) := null
          te.onCoversChanged()
          coverChanged(world, pos, side)
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
          oldCover <- te.covers(side)
          oldItem <- Misc.asInstanceOpt(oldCover.getItem, classOf[ItemCover])
        } {
          ItemUtils.throwItemAt(world, pos.offset(side), oldItem.onRemove(oldCover))
        }

        te.covers(side).set(coverItem.onInstall(te, side, activeStack.splitStack(1), player.asInstanceOf[EntityPlayerMP]))

        if (activeStack.stackSize <= 0)
          player.inventory.setInventorySlotContents(player.inventory.currentItem, null)

        te.onCoversChanged()
        coverChanged(world, pos, side)
      }
      return true
    }

    // If has cover - let it handle the click
    for ((item, stack, te) <- getCoverItem(world, pos, side)) {
      if (item.clickCover(te, side, stack, player)) return true
    }

    super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ)
  }

  override def canProvidePower = true

  override def getWeakPower(world: IBlockAccess, pos: BlockPos, state: IBlockState, side: EnumFacing): Int = {
    for ((item, stack, te) <- getCoverItem(world, pos, side.getOpposite)) {
      if (item.isEmittingSignal(te, side.getOpposite, stack)) return 15
    }
    0
  }

  def coverChanged(w: World, pos: BlockPos, side: EnumFacing): Unit = {
    val neighbour = pos.offset(side)
    w.notifyBlockOfStateChange(pos.offset(side), this)
  }

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState) = {
    if (!world.isRemote) {
      val te = getTE(world, pos)
      for {
        (dir, slot) <- te.covers
        cover <- slot
        item <- Misc.asInstanceOpt(cover.getItem, classOf[ItemCover])
      } {
        te.covers(dir) := null
        ItemUtils.throwItemAt(world, pos, item.onRemove(cover))
        coverChanged(world, pos, dir)
      }
    }
    super.breakBlock(world, pos, state)
  }
}
