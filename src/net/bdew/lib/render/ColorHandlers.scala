/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render

import net.bdew.lib.block.BaseBlock
import net.bdew.lib.items.BaseItem
import net.bdew.lib.{BdLib, Client}
import net.minecraft.client.renderer.color.{IBlockColor, IItemColor}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object ColorHandlers {
  var itemHandlers = Map.empty[BaseItem, IItemColor]
  var blockHandlers = Map.empty[BaseBlock, IBlockColor]

  def register(item: BaseItem, handler: IItemColor) = {
    if (Client.itemColors != null)
      Client.itemColors.registerItemColorHandler(handler, item)
    else
      itemHandlers += (item -> handler)
  }

  def register(block: BaseBlock, handler: IBlockColor) = {
    if (Client.blockColors != null)
      Client.blockColors.registerBlockColorHandler(handler, block)
    else
      blockHandlers += (block -> handler)
  }

  def addDelayed(): Unit = {
    BdLib.logInfo("Adding delayed color handlers")
    for ((item, handler) <- itemHandlers)
      Client.itemColors.registerItemColorHandler(handler, item)
    for ((block, handler) <- blockHandlers)
      Client.blockColors.registerBlockColorHandler(handler, block)
    itemHandlers = Map.empty
    blockHandlers = Map.empty
  }
}
