/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.block

import java.util

import net.minecraft.block.Block
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.world.World

trait BlockTooltip extends Block with HasItemBlock {
  override val itemBlockInstance: ItemBlock = new ItemBlockTooltip(this)
  def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[String]
}

class ItemBlockTooltip(b: Block) extends ItemBlock(b) {
  setRegistryName(b.getRegistryName)

  override def addInformation(stack: ItemStack, world: World, tooltip: util.List[String], flags: ITooltipFlag): Unit = {
    import scala.collection.JavaConversions._
    if (b.isInstanceOf[BlockTooltip])
      tooltip.addAll(b.asInstanceOf[BlockTooltip].getTooltip(stack, world, flags))
  }
}
