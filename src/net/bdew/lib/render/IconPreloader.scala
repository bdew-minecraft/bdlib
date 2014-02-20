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

/**
 * Allows loading of texture that don't belong to specific block or item into vanilla block/item atlas
 * @param kind 0=Blocks / 1=Items
 */
abstract class IconPreloader(kind: Int) {
  def registerIcons(reg: IconRegister)

  def init() {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @ForgeSubscribe
  def preTextureStitch(ev: TextureStitchEvent.Pre) {
    if (ev.map.getTextureType == kind)
      registerIcons(ev.map)
  }
}
