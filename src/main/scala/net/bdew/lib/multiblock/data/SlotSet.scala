/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.data

import net.bdew.lib.gui.Texture
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

abstract class SlotSet(val outputConfigId: String) {
  // outputConfigId should be registered in OutputConfigManager

  var slotMap = Map.empty[String, Slot]

  case class Slot(id: String, name: String) {
    slotMap += id -> this
    def next: Slot = order(this)

    @OnlyIn(Dist.CLIENT)
    def texture: Texture = textures(this)
  }

  def default: Slot
  val order: Map[Slot, Slot]

  @OnlyIn(Dist.CLIENT)
  def textures: Map[Slot, Texture]

  def get(id: String): Slot = slotMap(id)
}
