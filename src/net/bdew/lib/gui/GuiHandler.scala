/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

import scala.collection.mutable

class GuiHandler extends IGuiHandler {
  val GUIs = mutable.Map.empty[Int, GuiProvider]

  def register(p: GuiProvider) = {
    if (GUIs.isDefinedAt(p.guiId))
      sys.error("GUI ID Collision - %d was registered for %s and attempted to register by %s".format(p.guiId, GUIs(p.guiId), p))
    GUIs += (p.guiId -> p)
  }

  def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
    GUIs(ID).getContainer(world.getTileEntity(new BlockPos(x, y, z)), player)

  def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
    GUIs(ID).getGui(world.getTileEntity(new BlockPos(x, y, z)), player)
}
