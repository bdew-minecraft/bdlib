/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.gui

import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import cpw.mods.fml.relauncher.{Side, SideOnly}

trait GuiProvider {
  def guiId: Int

  type TEClass

  @SideOnly(Side.CLIENT)
  def getGui(te: TEClass, player: EntityPlayer): GuiContainer
  def getContainer(te: TEClass, player: EntityPlayer): Container

  @SideOnly(Side.CLIENT)
  def getGui(te: TileEntity, player: EntityPlayer): GuiContainer = getGui(te.asInstanceOf[TEClass], player)
  def getContainer(te: TileEntity, player: EntityPlayer): Container = getContainer(te.asInstanceOf[TEClass], player)
}
