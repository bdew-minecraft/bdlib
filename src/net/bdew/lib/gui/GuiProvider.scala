/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

trait GuiProvider {
  def guiId: Int

  type TEClass

  @SideOnly(Side.CLIENT)
  def getGui(te: TEClass, player: EntityPlayer): AnyRef
  def getContainer(te: TEClass, player: EntityPlayer): AnyRef

  @SideOnly(Side.CLIENT)
  def getGui(te: TileEntity, player: EntityPlayer): AnyRef =
    if (te != null)
      getGui(te.asInstanceOf[TEClass], player)
    else null

  def getContainer(te: TileEntity, player: EntityPlayer): AnyRef =
    if (te != null)
      getContainer(te.asInstanceOf[TEClass], player)
    else null
}
