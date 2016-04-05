/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import java.util

import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemBlock, ItemStack}

trait BlockTooltip extends Block with HasItemBlock {
  override val itemBlockInstance: ItemBlock = new ItemBlockTooltip(this)
  def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String]
}

class ItemBlockTooltip(b: Block) extends ItemBlock(b) {
  setRegistryName(b.getRegistryName)
  override def addInformation(stack: ItemStack, player: EntityPlayer, tooltip: util.List[String], advanced: Boolean) = {
    import scala.collection.JavaConversions._
    if (b.isInstanceOf[BlockTooltip])
      tooltip.addAll(b.asInstanceOf[BlockTooltip].getTooltip(stack, player, advanced))
  }
}
