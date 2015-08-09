/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.items

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.Item

trait NamedItem extends Item {
  def name: String
}

class SimpleItem(val name: String) extends Item with NamedItem {
  val modId = Misc.getActiveModId
  setUnlocalizedName(modId + "." + name)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    itemIcon = reg.registerIcon(Misc.iconName(modId, name))
  }
}
