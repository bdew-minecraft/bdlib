/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.config

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.Configuration

class IdManager(cfg: Configuration, itemStart: Int, blockStart: Int) {

  class MyIter(start: Int, check: Array[_], offset: Int = 0) {
    var now = start
    def next(): Int = {
      while (check(now + offset) != null) now += 1
      return now
    }
  }

  def getBlockId(name: String, comment: String) = cfg.getBlock(name, blockIds.next(), comment).getInt
  def getBlockId(name: String) = cfg.getBlock(name, blockIds.next()).getInt

  def getItemId(name: String, comment: String) = cfg.getItem(name, itemIds.next(), comment).getInt
  def getItemId(name: String) = cfg.getItem(name, itemIds.next()).getInt

  val blockIds = new MyIter(blockStart, Block.blocksList)
  val itemIds = new MyIter(itemStart, Item.itemsList, 256)

  // figure out what's the max id we already use, because forge is stupid
  // ignore ids in the top slots as they were probably assigned to resolve conflicts

  import scala.collection.JavaConverters._

  val maxBlock = (cfg.getCategory(Configuration.CATEGORY_BLOCK).values().asScala.map(_.getInt).filter(_ < Block.blocksList.length - 100) ++ Seq(0)).max + 1
  val maxItem = (cfg.getCategory(Configuration.CATEGORY_ITEM).values().asScala.map(_.getInt).filter(_ < Item.itemsList.length - 1000) ++ Seq(0)).max + 1

  if (maxBlock > blockIds.now) blockIds.now = maxBlock
  if (maxItem > itemIds.now) itemIds.now = maxItem
}

