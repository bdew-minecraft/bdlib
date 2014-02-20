/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.render

import net.minecraft.client.renderer.texture.IconRegister
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ForgeSubscribe
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraft.util.Icon

/**
 * Allows loading of texture that don't belong to specific block or item into vanilla block/item atlas
 * @param kind 0=Blocks / 1=Items
 */
class IconPreloader(kind: Int) {
  var icons = Set.empty[Entry]

  case class Entry(loc: String) {
    var icon: Icon = null
    icons += this
  }

  implicit def entry2icon(v: Entry) = v.icon

  def registerIcons(reg: IconRegister) {}

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @ForgeSubscribe
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == kind) {
      for (x <- icons) x.icon = ev.map.registerIcon(x.loc)
      registerIcons(ev.map)
    }
  }
}
