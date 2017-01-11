/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

class CreativeTabContainer {

  class Tab(name: String, iconStack: => ItemStack) extends CreativeTabs(name) {
    override def getTabIconItem = iconStack
  }

  BdLib.logInfo("Loaded creative tabs for %s", Misc.getActiveModId)

  def init() {}
}
