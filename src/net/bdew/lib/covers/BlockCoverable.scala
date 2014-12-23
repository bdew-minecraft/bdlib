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
import net.bdew.lib.block.HasTE
import net.bdew.lib.items.ItemUtils
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

trait BlockCoverable[T <: TileCoverable] extends HasTE[T] {
  @SideOnly(Side.CLIENT)
  override def getRenderType = CoverRenderer.id

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, face: Int, xOffs: Float, yOffs: Float, zOffs: Float): Boolean = {
    val te = getTE(world, x, y, z)
    val side = Misc.forgeDirection(face)

    if (player.getCurrentEquippedItem == null && player.isSneaking) {
      for (cover <- Option(te.covers(side).value)) {
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
        for (cover <- Option(te.covers(side).value))
          ItemUtils.throwItemAt(world, te.xCoord + side.offsetX, te.yCoord + side.offsetY, te.zCoord + side.offsetZ, cover.copy())

        te.covers(side) := activeStack.splitStack(1)

        if (activeStack.stackSize <= 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null)
        te.onCoversChanged()
      }
      return true
    }

    super.onBlockActivated(world, x, y, z, player, face, xOffs, yOffs, zOffs)
  }

  def getCoverIcon(w: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection): Option[IIcon] =
    for {
      coverStack <- Option(getTE(w, x, y, z).covers(side).value)
      coverItemMaybe <- Option(coverStack.getItem)
      coverItem <- Misc.asInstanceOpt(coverItemMaybe, classOf[ItemCover])
    } yield coverItem.getCoverIcon(coverStack)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    if (!world.isRemote)
      for ((dir, covOpt) <- getTE(world, x, y, z).covers; cover <- Option(covOpt.value))
        ItemUtils.throwItemAt(world, x, y, z, cover)
    super.breakBlock(world, x, y, z, block, meta)
  }
}
