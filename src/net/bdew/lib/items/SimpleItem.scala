/*
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.items

import net.minecraft.item.Item
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc

class SimpleItem(id: Int, val name: String) extends Item(id) {
  val modId = Misc.getActiveModId
  setUnlocalizedName(modId + "." + name)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IconRegister) {
    itemIcon = reg.registerIcon(modId + ":" + name.toLowerCase)
  }
}
