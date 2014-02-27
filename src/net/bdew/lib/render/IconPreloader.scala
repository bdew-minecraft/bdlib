/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.render

import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.client.event.TextureStitchEvent
import net.bdew.lib.gui.{Texture, IconWrapper}
import cpw.mods.fml.common.eventhandler.SubscribeEvent

/**
 * Allows loading of texture that don't belong to specific block or item into vanilla block/item atlas
 * @param kind 0=Blocks / 1=Items
 */
class IconPreloader(kind: Int) {
  var icons = Set.empty[TextureLoc]

  val sheet = kind match {
    case 0 => Texture.BLOCKS
    case 1 => Texture.ITEMS
    case x => sys.error("Invalid spritesheet number: %d".format(x))
  }

  case class TextureLoc(loc: String) {
    var texture: IconWrapper = null
    icons += this
  }

  import language.implicitConversions

  implicit def entry2icon(v: TextureLoc) = v.texture.icon
  implicit def entry2texture(v: TextureLoc) = v.texture

  def registerIcons(reg: IIconRegister) {}

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == kind) {
      for (x <- icons) x.texture = Texture(sheet, ev.map.registerIcon(x.loc))
      registerIcons(ev.map)
    }
  }
}
