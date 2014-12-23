/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items.inventory

import cpw.mods.fml.common.Loader
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.BdLib
import net.bdew.lib.gui.GuiProvider
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.world.World

/**
 * Mixin for items that have an internal inventory
 * Subclasses are responsible for registering themselves with the GuiHandler of their mod
 */
trait ItemInventory extends Item with GuiProvider {
  type TEClass = Any
  def guiId: Int
  val invSize: Int
  val invTagName: String
  val modObj = Loader.instance().activeModContainer().getMod

  @SideOnly(Side.CLIENT)
  def makeGui(inv: InventoryItemAdapter, player: EntityPlayer): AnyRef
  def makeContainer(inv: InventoryItemAdapter, player: EntityPlayer): ContainerItemInventory

  def makeAdaptor(player: EntityPlayer) = new InventoryItemAdapter(player, player.inventory.currentItem, invSize, invTagName)

  @SideOnly(Side.CLIENT)
  def getGui(te: TEClass, player: EntityPlayer) = {
    val stack = player.inventory.getCurrentItem
    if (stack.getItem == this) {
      makeGui(makeAdaptor(player), player)
    } else {
      BdLib.logWarn("Attempt to open item container GUI without active item (%s)", this)
      null
    }
  }

  def getContainer(te: TEClass, player: EntityPlayer) = {
    val stack = player.inventory.getCurrentItem
    if (stack.getItem == this) {
      makeContainer(makeAdaptor(player), player)
    } else {
      BdLib.logWarn("Attempt to open item container GUI without active item by '%s' (%s)", player.getCommandSenderName, this)
      null
    }
  }

  def doOpenGui(player: EntityPlayer, world: World) {
    player.openGui(modObj, guiId, world, player.posX.toInt, player.posY.toInt, player.posZ.toInt)
  }
}
