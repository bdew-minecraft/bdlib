/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

class CreativeTabContainer {

  class Tab(name: String, item: => Item) extends CreativeTabs(name) {
    override def getTabIconItem = item
  }

  BdLib.logInfo("Loaded creative tabs for %s", Misc.getActiveModId)

  def init() {}
}
